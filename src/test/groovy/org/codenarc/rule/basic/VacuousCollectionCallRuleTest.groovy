/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenarc.rule.basic

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for VacuousCollectionCallRule
 *
 * @author Hamlet D'Arcy
 * @version $Revision: 329 $ - $Date: 2010-04-29 04:20:25 +0200 (Thu, 29 Apr 2010) $
 */
class VacuousCollectionCallRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == "VacuousCollectionCall"
    }

    void testNoViolations() {
        final SOURCE = '''
        	def x = [1, 2, 3]
            x.containsAll(y)
            x.containsAll(y, z)
            x.containsAll()

            x.retainAll(y)
            x.retainAll(y, z)
            x.retainAll()
        '''
        assertNoViolations(SOURCE)
    }

    void testScriptFailure() {
        final SOURCE = '''
        	def x = [1, 2, 3]
            x.containsAll(x)
            x.retainAll(x)
        '''
        assertTwoViolations(SOURCE,
                3, 'x.containsAll(x)',
                4, 'x.retainAll(x)')
    }

    void testClassFailure() {
        final SOURCE = '''
            class MyClass {
            	def x = [1, 2, 3]

                def foo() {
                    x.containsAll(x)
                    x.retainAll(x)
                }
            }
        '''
        assertTwoViolations(SOURCE,
                6, 'x.containsAll(x)',
                7, 'x.retainAll(x)')
    }

    protected Rule createRule() {
        return new VacuousCollectionCallRule()
    }

}
