/**
 * Copyright (c) 2011-2014 James Coleman
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package homo.efficio.tomcat.extensions.session.redis;

import org.apache.catalina.util.CustomObjectInputStream;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;

public class JavaSerializer implements Serializer {
    private ClassLoader loader;

    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public void setClassLoader(ClassLoader loader) {
        this.loader = loader;
    }

    public byte[] attributesHashFrom(RedisSession session) throws IOException {
        HashMap<String,Object> attributes = new HashMap<String,Object>();
        for (Enumeration<String> enumerator = session.getAttributeNames(); enumerator.hasMoreElements();) {
            String key = enumerator.nextElement();
            attributes.put(key, session.getAttribute(key));
        }

        byte[] serialized = null;

        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(bos));
        ) {
            oos.writeUnshared(attributes);
            oos.flush();
            serialized = bos.toByteArray();
        }

        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("Unable to get MessageDigest instance for MD5");
        }
        return digester.digest(serialized);
    }

    @Override
    public byte[] serializeFrom(RedisSession session, SessionSerializationMetadata metadata) throws IOException {
        byte[] serialized = null;

        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(bos));
        ) {
            oos.writeObject(metadata);
            session.writeObjectData(oos);
            oos.flush();
            serialized = bos.toByteArray();
        }

        return serialized;
    }

    @Override
    public void deserializeInto(byte[] data, RedisSession session, SessionSerializationMetadata metadata) throws IOException, ClassNotFoundException {
        try(
                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(data));
                ObjectInputStream ois = new CustomObjectInputStream(bis, loader);
        ) {
            SessionSerializationMetadata serializedMetadata = (SessionSerializationMetadata)ois.readObject();
            metadata.copyFieldsFrom(serializedMetadata);
            session.readObjectData(ois);
        }
    }
}
