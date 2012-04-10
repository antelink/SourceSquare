/**
 * Copyright (C) 2009-2012 Antelink SAS
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License Version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License Version 3
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License Version
 * 3 along with this program. If not, see http://www.gnu.org/licenses/agpl.html
 *
 * Additional permission under GNU AGPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it with
 * Eclipse Java development tools (JDT) or Jetty (or a modified version of these
 * libraries), containing parts covered by the terms of Eclipse Public License 1.0,
 * the licensors of this Program grant you additional permission to convey the
 * resulting work. Corresponding Source for a non-source form of such a combination
 * shall include the source code for the parts of Eclipse Java development tools
 * (JDT) or Jetty used as well as that of the covered work.
 */
package com.antelink.sourcesquare.badge;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.easymock.EasyMock;
import org.easymock.IAnswer;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.FilesIdentifiedEvent;
import com.antelink.sourcesquare.event.events.OSFilesFoundEvent;

public class TestBadgeProcessor {
	
	BadgesProcessor processor;
	EventBus bus;
	Integer count=0;
	
	TreeSet<File> files=new TreeSet<File>(Arrays.asList(new File(""),new File(""),new File(""),new File(""),new File(""),new File(""),new File(""),new File(""),new File(""),new File("")));
	
	
	@Before
	public void init(){
		bus=new EventBus();
		processor=new BadgesProcessor(bus);
		processor.bind();
		count=0;
	}

	@Test @Ignore
	// Ignored test: too asynchronous to be trusted...
	public void testBadgesOpenSourceSamurai(){
		Set<String> set=EasyMock.createMock(Set.class);
		expect(set.size()).andReturn(8).andAnswer(new IAnswer<Integer>() {

			@Override
			public Integer answer() throws Throwable {
				synchronized (count) {
					count++;
				}
				return null;
			}
		});
				
		bus.fireEvent(new OSFilesFoundEvent(set));
		bus.fireEvent(new FilesIdentifiedEvent(files));
		
		
		Collection<Badge> badges = processor.process();
		Badge badge = badges.iterator().next();
		assertEquals(Badge.OS_SAMURAI,badge);
	}
	
}
