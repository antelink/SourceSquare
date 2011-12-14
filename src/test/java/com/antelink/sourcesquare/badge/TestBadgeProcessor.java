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
	@Test
	public void testBadgesOpenSourceJediMaster(){
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
		assertEquals(Badge.OS_JEDI_MASTER,badge);
	
		
	}
	
}
