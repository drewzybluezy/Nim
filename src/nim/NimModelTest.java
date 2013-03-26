package nim;

import junit.framework.TestCase;
/**
 * @author wainer
 * A small example to show what kind of test might be applied
 * to the model. There should be many tests to verify/design
 * the model's functionality.
 * 
 * This test is using JUnit 3.8.2.
 * In Eclipse you can run it, by highlighting this file in the
 * Package Explorer, (or use the Run menu when viewing in the editor)
 * and selecting, "Run as JUnit Test"
 *
 */
public class NimModelTest extends TestCase {

	public void testGameOverAtStart() {
		int initNumOfMatchsticks = 3;
		NimModel model = new NimModel(initNumOfMatchsticks);
        assertFalse("Game should not be over, just started", model.gameOver());
	}

}
