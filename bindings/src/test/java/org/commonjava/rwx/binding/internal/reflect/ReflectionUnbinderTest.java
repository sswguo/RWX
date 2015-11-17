/*
 *  Copyright (c) 2010 Red Hat, Inc.
 *  
 *  This program is licensed to you under Version 3 only of the GNU
 *  General Public License as published by the Free Software 
 *  Foundation. This program is distributed in the hope that it will be 
 *  useful, but WITHOUT ANY WARRANTY; without even the implied 
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 *  PURPOSE.
 *  
 *  See the GNU General Public License Version 3 for more details.
 *  You should have received a copy of the GNU General Public License 
 *  Version 3 along with this program. 
 *  
 *  If not, see http://www.gnu.org/licenses/.
 */

package org.commonjava.rwx.binding.internal.reflect;

import org.commonjava.rwx.binding.error.BindException;
import org.commonjava.rwx.binding.mapping.Mapping;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse;
import org.commonjava.rwx.binding.testutil.ComposedPersonResponse3;
import org.commonjava.rwx.binding.testutil.InheritedPersonRequest;
import org.commonjava.rwx.binding.testutil.RecordedEvent;
import org.commonjava.rwx.binding.testutil.RecordingListener;
import org.commonjava.rwx.binding.testutil.SimpleAddress;
import org.commonjava.rwx.binding.testutil.SimpleAddressMapResponse;
import org.commonjava.rwx.binding.testutil.SimpleConverterRequest;
import org.commonjava.rwx.binding.testutil.SimpleListRequest;
import org.commonjava.rwx.binding.testutil.SimplePersonRequest;
import org.commonjava.rwx.binding.testutil.TestObject;
import org.commonjava.rwx.error.XmlRpcException;
import org.commonjava.rwx.error.XmlRpcFaultException;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.estream.model.ResponseEvent;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.commonjava.rwx.binding.testutil.EventAssertions.assertRecordedEvents;

public class ReflectionUnbinderTest
{

    @Test( expected = BindException.class )
    public void invalidEntryPoint()
        throws XmlRpcException
    {
        new ReflectionUnbinder( new SimpleAddress(), new HashMap<Class<?>, Mapping<?>>() ).generate( new RecordingListener() );
    }

    @Test
    public void fault()
        throws XmlRpcException
    {
        final List<Event<?>> check = new ArrayList<Event<?>>(
                Arrays.asList( new ResponseEvent( true ), new ResponseEvent( 101, "Test fault" ),
                               new ResponseEvent( false ) ) );

        final ReflectionUnbinder unbinder = new ReflectionUnbinder( new XmlRpcFaultException( 101, "Test fault" ),
                                                                    new HashMap<Class<?>, Mapping<?>>() );
        final RecordingListener listener = new RecordingListener();
        unbinder.generate( listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        assertRecordedEvents( check, events );
    }

    @Test
    public void simpleRequest()
        throws XmlRpcException
    {
        assertUnbind( new SimplePersonRequest() );
    }

    @Test
    public void simpleConverterRequest()
        throws XmlRpcException, ParseException
    {
        assertUnbind( new SimpleConverterRequest() );
    }

    @Test
    public void simpleListRequest()
        throws XmlRpcException
    {
        assertUnbind( new SimpleListRequest() );
    }

    @Test
    public void simpleAddressMapResponse()
        throws XmlRpcException
    {
        assertUnbind( new SimpleAddressMapResponse() );
    }

    @Test
    public void requestWithInheritedFields()
        throws XmlRpcException
    {
        assertUnbind( new InheritedPersonRequest() );
    }

    @Test
    public void responseWithStructParam()
        throws XmlRpcException
    {
        assertUnbind( new ComposedPersonResponse() );
    }

    @Test
    public void responseWithArrayParam()
        throws XmlRpcException
    {
        assertUnbind( new ComposedPersonResponse3() );
    }

    private void assertUnbind( final TestObject request )
        throws XmlRpcException
    {
        final ReflectionUnbinder unbinder = new ReflectionUnbinder( request, request.recipes() );

        final RecordingListener listener = new RecordingListener();
        unbinder.generate( listener );

        final List<RecordedEvent> events = listener.getRecordedEvents();

        assertRecordedEvents( request.events(), events );
    }

}