package edu.uoc.elc.spring.lti.ags;

import edu.uoc.elc.Config;
import edu.uoc.elc.lti.tool.Registration;
import edu.uoc.elc.lti.tool.ResourceLink;
import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elc.lti.tool.oidc.InMemoryOIDCLaunchSession;
import edu.uoc.elc.spring.lti.security.LTIAccessTokenProvider;
import edu.uoc.elc.spring.lti.tool.ToolDefinitionBean;
import edu.uoc.elc.spring.lti.tool.builders.ClaimAccessorService;
import edu.uoc.elc.spring.lti.tool.registration.RegistrationService;
import edu.uoc.lti.ags.LineItem;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.oidc.OIDCLaunchSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.MethodNotAllowedException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class})
public class RestTemplateLineItemServiceClientTest {

	@Autowired
	private ToolDefinitionBean toolDefinitionBean;

	@Autowired
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	private RegistrationService registrationService;

	@Autowired
	private ClaimAccessorService claimAccessorService;

	@Autowired
	private String lineItemsUri;

	// subject under test
	private RestTemplateLineItemServiceClient sut;

	@Before
	public void setUp() throws URISyntaxException {
		Assert.assertNotNull(toolDefinitionBean);
		OIDCLaunchSession launchSession = new InMemoryOIDCLaunchSession();
		final Registration registration =  registrationService.getRegistration("id");
		final Tool tool = new Tool(registration,
						claimAccessorService.getClaimAccessor(registration),
						launchSession,
						toolDefinitionBean.getBuilders(registration));
		Assert.assertNotNull(tool);

		// spy tool
		Tool spy = Mockito.spy(tool);
		Mockito.when(spy.isValid()).thenReturn(true);
		Mockito.when(spy.getResourceLink()).thenReturn(new ResourceLink());

		final LTIAccessTokenProvider ltiAccessTokenProvider = new LTIAccessTokenProvider(spy);
		RestTemplateFactory restTemplateFactory = new TestRestTemplateFactory();
		RestTemplateLineItemServiceClientFactory factory = new RestTemplateLineItemServiceClientFactory(restTemplateFactory);
		this.sut = factory.of(ltiAccessTokenProvider);
		this.sut.setServiceUri(new URI(lineItemsUri));
		Assert.assertNotNull(sut);
	}

	private static final String TEST_LABEL = "Test Label";
	private static final String TEST_TAG = "test";
	private static final Double TEST_SCORE_MAXIMUM = 1.0;

	@After
	public void tearDown() {
		// delete all test lineItems
		try {
			final List<LineItem> lineItems = this.sut.getLineItems(null, null, null, null, null);
			for (LineItem lineItem : lineItems) {
				if (TEST_LABEL.equals(lineItem.getLabel()) || TEST_TAG.equals(lineItem.getTag())) {
					this.sut.deleteLineItem(lineItem.getId());
				}
			}
		} catch (MethodNotAllowedException ignored) {

		}
	}

	private LineItem lineItem(String label, double scoreMaximum) {
		LineItem lineItem = new LineItem();
		lineItem.setLabel(label);
		lineItem.setScoreMaximum(scoreMaximum);
		lineItem.setTag(TEST_TAG);
		return lineItem;
	}

	@Test
	public void getLineItems() {
		final List<LineItem> lineItems = this.sut.getLineItems(null, null, null, null , null);
		Assert.assertNotNull(lineItems);
	}

	private void assertNewLineItem(LineItem expected, LineItem actual) {
		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
		assertEquals(expected, actual, false);
	}
	private void assertEquals(LineItem expected, LineItem actual) {
		assertEquals(expected, actual, true);
	}

	private void assertEquals(LineItem expected, LineItem actual, boolean assertId) {
		if (assertId) {
			Assert.assertEquals(expected.getId(), actual.getId());
		}
		Assert.assertEquals(expected.getLabel(), actual.getLabel());
		Assert.assertEquals(expected.getScoreMaximum(), actual.getScoreMaximum(), 0);
		Assert.assertEquals(expected.getTag(), actual.getTag());
		Assert.assertEquals(expected.getResourceId(), actual.getResourceId());
		Assert.assertEquals(expected.getResourceLinkId(), actual.getResourceLinkId());
		Assert.assertEquals(expected.getSubmission(), actual.getSubmission());
	}

	private int lineItemsSize() {
		// get previous line items
		final List<LineItem> lineItems = this.sut.getLineItems(null, null, null, null, null);
		Assert.assertNotNull(lineItems);
		return lineItems.size();
	}

	private LineItem createAndAssertNewLineItem() {
		LineItem lineItem = lineItem(TEST_LABEL, TEST_SCORE_MAXIMUM);
		final LineItem newLineItem = this.sut.createLineItem(lineItem);
		assertNewLineItem(lineItem, newLineItem);
		return newLineItem;
	}

	@Test
	public void createLineItem() {
		// get previous line items
		final int previousSize = lineItemsSize();

		createAndAssertNewLineItem();

		// get after line items
		final int afterSize = lineItemsSize();
		Assert.assertEquals(afterSize, previousSize + 1);
	}

	@Test
	public void getLineItem() {
		final LineItem newLineItem = createAndAssertNewLineItem();

		final LineItem gottenLineItem = this.sut.getLineItem(newLineItem.getId());
		Assert.assertNotNull(gottenLineItem);
		assertEquals(newLineItem, gottenLineItem);
	}

	@Test
	public void updateLineItem() {
		final LineItem newLineItem = createAndAssertNewLineItem();

		newLineItem.setLabel("Modified Label");
		final LineItem modifiedLineItem = this.sut.updateLineItem(newLineItem.getId(), newLineItem);
		Assert.assertNotNull(modifiedLineItem);
		assertEquals(newLineItem, modifiedLineItem);
	}

	@Test
	public void deleteLineItem() {
		// get previous line items
		final int previousSize = lineItemsSize();

		final LineItem newLineItem = createAndAssertNewLineItem();

		// get after line items
		int afterSize = lineItemsSize();
		Assert.assertEquals(afterSize, previousSize + 1);

		// delete line item
		this.sut.deleteLineItem(newLineItem.getId());

		// get after line items
		afterSize = lineItemsSize();
		Assert.assertEquals(afterSize, previousSize);
	}
}
