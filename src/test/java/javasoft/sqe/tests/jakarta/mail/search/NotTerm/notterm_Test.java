/*
 * Copyright (c) 2002, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package javasoft.sqe.tests.jakarta.mail.search.NotTerm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.search.*;
import com.sun.javatest.*;
import javasoft.sqe.tests.jakarta.mail.util.MailTest;

/**
 * This class tests the <strong>NotTerm()</strong> API.
 * It does this by passing various valid input values and then checking
 * the type of the returned object.	<p>
 *
 *		A contructor that takes one SearchTerm argument. <p>
 * api2test: public NotTerm(SearchTerm)  <p>
 *		get search term to negate  <p>
 * api2test: public SearchTerm getTerm()  <p>
 *		The match method.   <p>
 * api2test: boolean match(Message)  <p>
 *
 * how2test: Call these APIs with pattern/string/message arguments and if
 *	     'match' returns boolean value, then this testcase passes.
 */

public class notterm_Test extends MailTest {

    @org.junit.jupiter.api.Test
    public void test() {
        Status s = run();
        assertEquals(Status.PASSED, s.getType(), "Status " + s);
    }

    public Status run()
    {
    

        out.fine("\nTesting class NotTerm: NotTerm(SearchTerm)\n");

        try {
          // Connect to host server
             Store store = connect2host(protocol, host, user, password);

          // Get a Folder object
	     Folder root = getRootFolder(store);
             Folder folder = root.getFolder(mailbox);

             if ( folder == null ) {
                  return Status.failed("Invalid folder object!");
             }
             folder.open(Folder.READ_ONLY);

	     out.fine("UNIT TEST 1: NotTerm(SearchTerm)");

             NotTerm not = new NotTerm(new SubjectTerm(subject));	// API TEST

	     if( not != null )
	     	 out.fine("UNIT TEST 1: passed.");
	     else {
		   out.fine("UNIT TEST 1: FAILED.");
		   errors++;
	     }
	     out.fine("UNIT TEST 2: getTerm()");

	     // get the negate term
             SearchTerm negateTerm = not.getTerm();        // API TEST

	     if( negateTerm != null ) {
		 out.fine(negateTerm.toString());
		 out.fine("UNIT TEST 2: passed.");
	     }

	     if( msgcount == -1 ) {
                 msgcount = folder.getMessageCount();
                 if( msgcount < 1 )
                     return Status.failed("Mail folder is empty!");
             }
	     boolean foundit = false;

             for( int i = 1; i <= msgcount; i++ )
             {
             // Get the message
                MimeMessage msg =  (MimeMessage)folder.getMessage(i);

	        if( msg == null ) {
		    log.warning("WARNING: FAILED TO GET MESSAGE NUMBER: "+ i);
		    continue;
	        }
	     // BEGIN UNIT TEST:
	        out.fine("UNIT TEST "+ (i+2) +":  match(Message)");

		// find the pattern in message body
		foundit = not.match(msg);	// API TEST

	        if( foundit ) {
	            out.fine("Atleast one negate term found in the message header.");
                    out.fine("UNIT TEST "+ (i+2) +":  passed\n");
	        } else {
		        out.fine("No negate terms found in the message header!");
			out.fine("UNIT TEST "+ (i+2) +":  passed\n");
	        }
	     // END UNIT TEST:
	     }
	     folder.close(false);
	     store.close();
             checkStatus();

        } catch ( Exception e ) {
             handlException(e);
        }
	return status;
     }
}
