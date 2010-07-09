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

package com.redhat.xmlrpc.raw.model;

import com.redhat.xmlrpc.raw.error.XmlRpcFaultException;

public class XmlRpcFault
{

    private final int code;

    private final String message;

    public XmlRpcFault( final int code, final String message )
    {
        this.code = code;
        this.message = message;
    }

    public void throwIt()
        throws XmlRpcFaultException
    {
        throw new XmlRpcFaultException( this );
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }

}
