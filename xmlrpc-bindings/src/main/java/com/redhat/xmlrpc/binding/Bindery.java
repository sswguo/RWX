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

package com.redhat.xmlrpc.binding;

import com.redhat.xmlrpc.error.XmlRpcException;
import com.redhat.xmlrpc.spi.XmlRpcGenerator;
import com.redhat.xmlrpc.spi.XmlRpcListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public interface Bindery
{

    <T> T parse( final InputStream in, final Class<T> type )
        throws XmlRpcException;

    <T> T parse( final Reader in, final Class<T> type )
        throws XmlRpcException;

    <T> T parse( final String in, final Class<T> type )
        throws XmlRpcException;

    <T> T parse( final XmlRpcGenerator in, final Class<T> type )
        throws XmlRpcException;

    void render( final OutputStream out, final Object value )
        throws XmlRpcException;

    void render( final Writer out, final Object value )
        throws XmlRpcException;

    void render( final XmlRpcListener out, final Object value )
        throws XmlRpcException;

    String renderString( final Object value )
        throws XmlRpcException;

}