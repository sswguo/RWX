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

package org.commonjava.rwx.vocab;

import org.apache.commons.codec.binary.Base64;
import org.commonjava.rwx.error.CoercionException;
import org.commonjava.rwx.util.ValueCoercion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public enum ValueType
{
    STRUCT( new ValueCoercion()
    {
        @Override
        public String toString( final Object value )
            throws CoercionException
        {
            throw new CoercionException( "Cannot coerce STRUCT types." );
        }

        @Override
        public Object fromString( final String value )
            throws CoercionException
        {
            throw new CoercionException( "Cannot coerce STRUCT types." );
        }
    }, Map.class, XmlRpcConstants.STRUCT ),

    ARRAY( new ValueCoercion()
    {
        @Override
        public String toString( final Object value )
            throws CoercionException
        {
            throw new CoercionException( "Cannot coerce ARRAY types." );
        }

        @Override
        public Object fromString( final String value )
            throws CoercionException
        {
            throw new CoercionException( "Cannot coerce ARRAY types." );
        }
    }, List.class, XmlRpcConstants.ARRAY ),

    NIL( new ValueCoercion()
    {
        @Override
        public Object fromString( final String value )
            throws CoercionException
        {
            return null;
        }

        @Override
        public String toString( final Object value )
        {
            return null;
        }
    }, Void.class, "nil" ),

    INT( new ValueCoercion()
    {
        @Override
        public Object fromString( final String value )
            throws CoercionException
        {
            try
            {
                return value == null ? null : Integer.parseInt( value );
            }
            catch ( final NumberFormatException e )
            {
                throw new CoercionException( e.getMessage(), e );
            }
        }
    }, Integer.class, "int", "i4" ),

    BOOLEAN( new ValueCoercion()
    {
        @Override
        public String toString( final Object value )
            throws CoercionException
        {
            return value == null ? null : Boolean.valueOf( super.toString( value ) ) ? "1" : "0";
        }

        @Override
        public Object fromString( final String value )
        {
            if ( value == null )
            {
                return null;
            }
            else
            {
                return "1".equals( value ) || Boolean.valueOf( value );
            }
        }
    }, Boolean.class, "boolean" ),

    STRING( new ValueCoercion()
    {
        @Override
        public Object fromString( final String value )
        {
            return value;
        }
    }, String.class, "string" ),

    DOUBLE( new ValueCoercion()
    {
        @Override
        public Object fromString( final String value )
            throws CoercionException
        {
            try
            {
                return value == null ? null : Double.parseDouble( value );
            }
            catch ( final NumberFormatException e )
            {
                throw new CoercionException( e.getMessage(), e );
            }
        }

        @Override
        public String toString( final Object value )
        {
            return value == null ? null : Double.toString( ( (Number) value ).doubleValue() );
        }
    }, Number.class, "double" ),

    DATETIME( new ValueCoercion()
    {
        private static final String FORMAT = "yyyyMMdd'T'HHmmss";

        @Override
        public Object fromString( final String value )
            throws CoercionException
        {
            try
            {
                return value == null ? null : new SimpleDateFormat( FORMAT ).parse( value );
            }
            catch ( final ParseException e )
            {
                throw new CoercionException( "Cannot parse date: '" + value + "'.", e );
            }
        }

        @Override
        public String toString( final Object value )
            throws CoercionException
        {
            try
            {
                return value == null ? null : new SimpleDateFormat( FORMAT ).format( (Date) value );
            }
            catch ( final ClassCastException e )
            {
                throw new CoercionException( "Not a java.util.Date.", e );
            }
        }

    }, Date.class, "dateTime.iso8601" ),

    BASE64( new ValueCoercion()
    {
        @Override
        public Object fromString( final String value )
            throws CoercionException
        {
            if ( value == null )
            {
                return null;
            }

            final byte[] result = Base64.decodeBase64( value );
            if ( result.length < 1 && value.length() > 0 && !value.equals( "==" ) && !value.equals( "=" ) )
            {
                throw new CoercionException( "Invalid Base64 input: " + value );
            }

            return result;
        }

        @Override
        public String toString( final Object value )
            throws CoercionException
        {
            try
            {
                return value == null ? null
                                : new String(
                                              Base64.encodeBase64( value instanceof String ? ( (String) value ).getBytes()
                                                              : (byte[]) value ) );
            }
            catch ( final ClassCastException e )
            {
                throw new CoercionException( "Not a byte array.", e );
            }
        }

    }, byte[].class, "base64" ), ;

    private String[] tags;

    private Class<?> nativeType;

    private ValueCoercion coercion;

    private ValueType( final ValueCoercion coercion, final Class<?> nativeType, final String... tags )
    {
        this.coercion = coercion;
        this.nativeType = nativeType;
        this.tags = tags;
    }

    public ValueCoercion coercion()
    {
        return coercion;
    }

    public String getPrimaryTag()
    {
        return tags[0];
    }

    public static ValueType safeTypeFor( final Object value )
    {
        final ValueType type = typeFor( value );
        return type == null ? STRING : type;
    }

    public static ValueType typeFor( final Object value )
    {
        ValueType result = null;

        if ( value == null )
        {
            result = NIL;
        }
        else
        {
            final Class<?> cls = value instanceof Class<?> ? (Class<?>) value : value.getClass();
            for ( final ValueType vt : values() )
            {
                if ( vt.nativeType.isAssignableFrom( cls ) )
                {
                    result = vt;
                    break;
                }
            }
        }

        return result;
    }

    public static ValueType typeOf( final String tag )
    {
        if ( tag == null || tag.trim().length() < 1 )
        {
            return STRING;
        }

        for ( final ValueType type : values() )
        {
            for ( final String t : type.tags )
            {
                if ( t.equals( tag ) )
                {
                    return type;
                }
            }
        }

        return STRING;
    }
}