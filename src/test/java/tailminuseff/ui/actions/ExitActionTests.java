package tailminuseff.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import mockit.*;

import org.junit.*;

public class ExitActionTests {

	@Before
	public void setUp() throws Exception {
		new MockUp<System>() {
			@Mock
			public void exit(int code) {
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test(@Mocked Component eventSource, @Mocked System system) {
		new ExitAction().actionPerformed(new ActionEvent(eventSource, 0, ""));

		new Verifications() {
			{
				System.exit(0);
				times = 1;
			}
		};
	}
}
