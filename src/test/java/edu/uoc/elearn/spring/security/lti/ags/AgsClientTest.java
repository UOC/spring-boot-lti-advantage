package edu.uoc.elearn.spring.security.lti.ags;

import edu.uoc.elc.lti.platform.ags.LineItem;
import edu.uoc.elc.lti.tool.AssignmentGradeService;
import edu.uoc.elc.lti.tool.Tool;
import edu.uoc.elearn.Config;
import edu.uoc.elearn.spring.security.lti.tool.ToolDefinition;
import edu.uoc.elearn.spring.security.lti.tool.ToolProvider;
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

	private AssignmentGradeService assignmentGradeService;

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
}
