/*
 *  Copyright (C) 2010 John Casey.
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.xmlrpc.impl.stax.helper;

import static com.redhat.xmlrpc.impl.estream.testutil.EventAssertions.assertRecordedEvents;

import org.jdom.JDOMException;
import org.junit.Test;

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.impl.estream.model.ArrayEvent;
import com.redhat.xmlrpc.impl.estream.model.Event;
import com.redhat.xmlrpc.impl.estream.model.StructEvent;
import com.redhat.xmlrpc.impl.estream.model.ValueEvent;
import com.redhat.xmlrpc.impl.estream.testutil.ExtList;
import com.redhat.xmlrpc.impl.estream.testutil.ExtMap;
import com.redhat.xmlrpc.impl.estream.testutil.RecordedEvent;
import com.redhat.xmlrpc.impl.estream.testutil.RecordingListener;
import com.redhat.xmlrpc.vocab.EventType;
import com.redhat.xmlrpc.vocab.ValueType;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.IOException;
import java.util.List;

public class ValueHelperTest
    extends AbstractStaxHelperTest
{

    @Test
    public void simpleString()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "simpleStringValue" );
        gotoElement( reader );

        final ValueHelper helper = new ValueHelper();

        final RecordingListener listener = new RecordingListener();
        helper.parse( reader, listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check = new ExtList<Event<?>>( new ValueEvent( "foo", ValueType.STRING ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleStructValue()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "structInValue" );
        gotoElement( reader );

        final RecordingListener listener = new RecordingListener();
        new ValueHelper().parse( reader, listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new StructEvent( EventType.START_STRUCT ), new StructEvent( "key" ),
                                   new ValueEvent( "foo", ValueType.STRING ), new StructEvent( "key", "foo",
                                                                                               ValueType.STRING ),
                                   new StructEvent( EventType.END_STRUCT_MEMBER ),
                                   new StructEvent( EventType.END_STRUCT ),
                                   new ValueEvent( new ExtMap<String, String>( "key", "foo" ), ValueType.STRUCT ) );

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleArrayValue()
        throws JDOMException, IOException, XMLStreamException, XmlRpcException
    {
        final XMLStreamReader reader = getXML( "arrayInValue" );
        gotoElement( reader );

        final RecordingListener listener = new RecordingListener();
        new ValueHelper().parse( reader, listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        final List<Event<?>> check =
            new ExtList<Event<?>>( new ArrayEvent( EventType.START_ARRAY ), new ArrayEvent( 0 ),
                                   new ValueEvent( "foo", ValueType.STRING ), new ArrayEvent( 0, "foo",
                                                                                              ValueType.STRING ),
                                   new ArrayEvent( EventType.END_ARRAY_ELEMENT ),
                                   new ArrayEvent( EventType.END_ARRAY ), new ValueEvent( new ExtList<String>( "foo" ),
                                                                                          ValueType.ARRAY ) );

        assertRecordedEvents( check, events );
    }

}