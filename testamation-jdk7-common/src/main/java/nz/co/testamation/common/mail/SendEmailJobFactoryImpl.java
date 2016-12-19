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

package nz.co.testamation.common.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

public class SendEmailJobFactoryImpl implements SendEmailJobFactory {

    private static final Logger logger = LoggerFactory.getLogger( SendEmailJobFactoryImpl.class );

    @Override
    public Runnable create( final Message message ) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Transport.send( message );
                } catch ( MessagingException e ) {
                    logger.error( "Failed to send email", e );
                    throw new RuntimeException( e );
                }
            }
        };

    }
}
