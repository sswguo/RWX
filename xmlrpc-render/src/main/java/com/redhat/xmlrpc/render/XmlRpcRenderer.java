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

package com.redhat.xmlrpc.render;

import com.redhat.xmlrpc.raw.error.XmlRpcException;
import com.redhat.xmlrpc.raw.model.XmlRpcFault;
import com.redhat.xmlrpc.raw.model.XmlRpcRequest;
import com.redhat.xmlrpc.raw.model.XmlRpcResponse;

public interface XmlRpcRenderer
{

    String render( XmlRpcRequest request )
        throws XmlRpcException;

    String render( XmlRpcResponse response )
        throws XmlRpcException;

    String render( XmlRpcFault fault )
        throws XmlRpcException;

}
