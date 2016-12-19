/*
 * Copyright 2016 Ratha Long
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.testamation.common.util;

import com.google.common.collect.ImmutableList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Test;

public class CollectionUtilTest {

    @Test
    public void getMaxHappyDay() throws Exception {
        DateTime one = new DateTime();
        DateTime two = one.plusSeconds( 1 );
        DateTime three = two.plusSeconds( 1 );

        DateTime actual = CollectionUtil.getMax( ImmutableList.of( one, three, two ) );
        MatcherAssert.assertThat( actual, Matchers.equalTo( three ) );


    }

    @Test
    public void getMaxReturnNullIfEmpty() throws Exception {
        DateTime actual = CollectionUtil.getMax( ImmutableList.<DateTime>of() );
        MatcherAssert.assertThat( actual, Matchers.nullValue() );


    }

}