package edu.uoc.elearn.spring.security.lti.ags;

import edu.uoc.elc.lti.platform.ags.LineItem;
import edu.uoc.elc.lti.platform.ags.Result;
import edu.uoc.elc.lti.platform.ags.ResultContainer;
import edu.uoc.elc.lti.tool.AssignmentGradeService;
import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elearn.Config;
import edu.uoc.elearn.spring.security.lti.tool.ToolDefinition;
import edu.uoc.elearn.spring.security.lti.tool.ToolProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class})
public class AgsClientTest {

	@Autowired
	private ToolDefinition toolDefinition;

	// mocked object
	private AssignmentGradeService assignmentGradeService;

	// subject under test
	private AgsClient sut;

	@Before
	public void setUp() {
		Assert.assertNotNull(toolDefinition);
		final Tool tool = new Tool(toolDefinition.getName(), toolDefinition.getClientId(), toolDefinition.getKeySetUrl(), toolDefinition.getAccessTokenUrl(), toolDefinition.getPrivateKey(), toolDefinition.getPublicKey());
		Assert.assertNotNull(tool);

		// mock service
		this.assignmentGradeService = Mockito.mock(AssignmentGradeService.class);
		Mockito.when(this.assignmentGradeService.getLineitems()).thenReturn("https://lti-ri.imsglobal.org/platforms/68/contexts/88/line_items");

		// spy tool
		Tool spy = Mockito.spy(tool);
		Mockito.when(spy.getAssignmentGradeService()).thenReturn(this.assignmentGradeService);
		Mockito.when(spy.isValid()).thenReturn(true);


		final ToolProvider toolProvider = new ToolProvider(spy);
		Assert.assertNotNull(toolProvider);
		this.sut = toolProvider.getAssignmentAndGradeService();
		Assert.assertNotNull(sut);
	}

	private static final String TEST_LABEL = "Test Label";
	private static final String TEST_TAG = "test";
	private static final Double TEST_SCORE_MAXIMUM = 1.0;

	@After
	public void tearDown() {
		// delete all test lineItems
		Mockito.when(this.assignmentGradeService.canManageLineItems()).thenReturn(true);
		Mockito.when(this.assignmentGradeService.canReadLineItems()).thenReturn(true);
		final List<LineItem> lineItems = this.sut.getLineItems(null, null, null, null, null);
		for (LineItem lineItem : lineItems) {
			if (TEST_LABEL.equals(lineItem.getLabel()) || TEST_TAG.equals(lineItem.getTag())) {
				this.sut.deleteLineItem(lineItem.getId());
			}
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
	public void getCanGetLineItems() {
		Mockito.when(this.assignmentGradeService.canReadLineItems()).thenReturn(true);
		Assert.assertTrue(this.sut.canReadLineItems());
		Mockito.verify(this.assignmentGradeService, Mockito.times(1)).canReadLineItems();
	}

	@Test
	public void getCantGetLineItems() {
		Mockito.when(this.assignmentGradeService.canReadLineItems()).thenReturn(false);
		Assert.assertFalse(this.sut.canReadLineItems());
		Mockito.verify(this.assignmentGradeService, Mockito.times(1)).canReadLineItems();
	}

	@Test
	public void getCanManageLineItems() {
		Mockito.when(this.assignmentGradeService.canManageLineItems()).thenReturn(true);
		Assert.assertTrue(this.sut.canManageLineItems());
		Mockito.verify(this.assignmentGradeService, Mockito.times(1)).canManageLineItems();
	}

	@Test
	public void getCantManageLineItems() {
		Mockito.when(this.assignmentGradeService.canManageLineItems()).thenReturn(false);
		Assert.assertFalse(this.sut.canManageLineItems());
		Mockito.verify(this.assignmentGradeService, Mockito.times(1)).canManageLineItems();
	}

	@Test
	public void getCanReadGrades() {
		Mockito.when(this.assignmentGradeService.canReadGrades()).thenReturn(true);
		Assert.assertTrue(this.sut.canReadGrades());
		Mockito.verify(this.assignmentGradeService, Mockito.times(1)).canReadGrades();
	}

	@Test
	public void getCantReadGrades() {
		Mockito.when(this.assignmentGradeService.canReadGrades()).thenReturn(false);
		Assert.assertFalse(this.sut.canReadGrades());
		Mockito.verify(this.assignmentGradeService, Mockito.times(1)).canReadGrades();
	}

	@Test
	public void getCanScore() {
		Mockito.when(this.assignmentGradeService.canScore()).thenReturn(true);
		Assert.assertTrue(this.sut.canScore());
		Mockito.verify(this.assignmentGradeService, Mockito.times(1)).canScore();
	}

	@Test
	public void getCantScore() {
		Mockito.when(this.assignmentGradeService.canScore()).thenReturn(false);
		Assert.assertFalse(this.sut.canScore());
		Mockito.verify(this.assignmentGradeService, Mockito.times(1)).canScore();
	}

	@Test
	public void getLineItems() {
		Mockito.when(this.assignmentGradeService.canReadLineItems()).thenReturn(true);

		final List<LineItem> lineItems = this.sut.getLineItems(null, null, null, null, null);
		Assert.assertNotNull(lineItems);

		Mockito.verify(this.assignmentGradeService, Mockito.times(1)).canReadLineItems();
	}

	private void assertNewLineItem(LineItem expected, LineItem actual) {
		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
		assertEquals(expected, actual, false);
		Mockito.verify(this.assignmentGradeService, Mockito.times(1)).canManageLineItems();
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
		Mockito.when(this.assignmentGradeService.canReadLineItems()).thenReturn(true);
		// get previous line items
		final List<LineItem> lineItems = this.sut.getLineItems(null, null, null, null, null);
		Assert.assertNotNull(lineItems);
		return lineItems.size();
	}

	private LineItem createAndAssertNewLineItem() {
		Mockito.when(this.assignmentGradeService.canManageLineItems()).thenReturn(true);
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
		Assert.assertTrue(afterSize == previousSize + 1);
	}

	@Test
	public void getLineItem() {
		Mockito.when(this.assignmentGradeService.canReadLineItems()).thenReturn(true);

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
		Assert.assertTrue(afterSize == previousSize + 1);

		// delete line item
		this.sut.deleteLineItem(newLineItem.getId());

		// get after line items
		afterSize = lineItemsSize();
		Assert.assertEquals(afterSize, previousSize);
	}


	@Test
	public void getResults() {
		Mockito.when(this.assignmentGradeService.canReadGrades()).thenReturn(true);

		final LineItem newLineItem = createAndAssertNewLineItem();
		final List<Result> lineItemResults = this.sut.getLineItemResults(newLineItem.getId(), null, null, null);
		Assert.assertNotNull(lineItemResults);
	}
}
