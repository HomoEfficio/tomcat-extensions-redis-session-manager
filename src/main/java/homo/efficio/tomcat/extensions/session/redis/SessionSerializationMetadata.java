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

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class SessionSerializationMetadata implements Serializable {
    private byte[] sessionAttributesHash;

    public SessionSerializationMetadata() {
        this.sessionAttributesHash = new byte[0];
    }

    public byte[] getSessionAttributesHash() {
        return sessionAttributesHash;
    }

    public void setSessionAttributesHash(byte[] sessionAttributesHash) {
        this.sessionAttributesHash = sessionAttributesHash;
    }

    public void copyFieldsFrom(SessionSerializationMetadata metadata) {
        this.setSessionAttributesHash(metadata.getSessionAttributesHash());
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(sessionAttributesHash.length);
        out.write(this.sessionAttributesHash);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        int hashLength = in.readInt();
        byte[] sessionAttributesHash = new byte[hashLength];
        in.read(sessionAttributesHash, 0, hashLength);
        this.sessionAttributesHash = sessionAttributesHash;
    }

    private void readObjectNoData() throws ObjectStreamException {
        this.sessionAttributesHash = new byte[0];
    }
}
