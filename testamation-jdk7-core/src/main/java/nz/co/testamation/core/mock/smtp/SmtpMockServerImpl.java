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

package nz.co.testamation.core.mock.smtp;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import nz.co.testamation.common.util.ReflectionUtil;
import nz.co.testamation.core.lifecycle.annotation.AfterGiven;
import nz.co.testamation.core.lifecycle.annotation.Reset;
import nz.co.testamation.core.lifecycle.annotation.TestLifeCycle;
import nz.co.testamation.core.waiting.Task;
import nz.co.testamation.core.waiting.WaitForNotEmpty;

import java.util.Collection;
import java.util.List;

@TestLifeCycle
public class SmtpMockServerImpl implements SmtpMockServer {

    private final SimpleSmtpServer simpleSmtpServer;
    private final SmtpMessageWrapperFactory smtpMessageWrapperFactory;

    public SmtpMockServerImpl(
        SimpleSmtpServerFactory simpleSmtpServerFactory,
        SmtpMessageWrapperFactory smtpMessageWrapperFactory
    ) {
        this.smtpMessageWrapperFactory = smtpMessageWrapperFactory;
        this.simpleSmtpServer = simpleSmtpServerFactory.create();
    }

    public SmtpMockServerImpl( int port ) {
        this( new SimpleSmtpServerFactoryImpl( port ), new SmtpMessageWrapperFactoryImpl() );
    }

    @Reset
    @AfterGiven
    public void reset() {
        ReflectionUtil.getFieldValue( simpleSmtpServer, "receivedMail", Collection.class ).clear();
    }

    @Override
    public List<SmtpMessageWrapper> getReceivedEmails() {
        List<SmtpMessage> smtpMessages = (List<SmtpMessage>) new WaitForNotEmpty<>()
            .when( new Task<Iterable>() {
                @Override
                public Iterable execute() {
                    return Lists.newArrayList( simpleSmtpServer.getReceivedEmail() );

                }
            } ).run();

        return Lists.transform( smtpMessages, new Function<SmtpMessage, SmtpMessageWrapper>() {
            @Override
            public SmtpMessageWrapper apply( SmtpMessage smtpMessage ) {
                return smtpMessageWrapperFactory.create( smtpMessage );
            }
        } );

    }

    @Override
    public SmtpMessageWrapper getReceivedEmail() {
        return Iterables.getOnlyElement( getReceivedEmails(), null );
    }
}
