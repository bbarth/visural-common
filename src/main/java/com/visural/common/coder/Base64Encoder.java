/*
 *  Copyright 2009 Richard Nichols.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.visural.common.coder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Basic Base64 encoder
 *
 * @version $Id: Base64Encoder.java 66 2010-07-13 08:59:50Z tibes80@gmail.com $
 * @author Richard Nichols
 */
public class Base64Encoder {

    /**
     * Thread safe public instance of a base 64 encoder which does not line break.
     */
    public static final Base64Encoder instance = new Base64Encoder();
    
    private static final byte[] encodeLookup = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
        's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '+', '/'};
    
    private final int lineBreakCharFrequency;
    private byte[] curEnc = new byte[4];

    public Base64Encoder() {
        this.lineBreakCharFrequency = 0;
    }

    public Base64Encoder(int lineBreakCharFrequency) {
        this.lineBreakCharFrequency = lineBreakCharFrequency;
    }   

    private void minorEncode(AtomicInteger lineCount, int nChars, int[] inBuf, OutputStream os) throws IOException {
        if (nChars > 0 && nChars <= 3) {
            switch (nChars) {
                case 1:
                    inBuf[1] = 0x00;
                    inBuf[2] = 0x00;
                    break;
                case 2:
                    inBuf[2] = 0x00;
                    break;
            }
            int offs0 = (inBuf[0] >>> 2);
            int offs1 = ((inBuf[0] & 0x00000003) << 4) + (inBuf[1] >>> 4);
            int offs2 = ((inBuf[1] & 0x0000000F) << 2) + (inBuf[2] >>> 6);
            int offs3 = (inBuf[2] & 0x0000003F);
            curEnc[0] = encodeLookup[offs0];
            curEnc[1] = encodeLookup[offs1];
            curEnc[2] = encodeLookup[offs2];
            curEnc[3] = encodeLookup[offs3];
            switch (nChars) {
                case 1:
                    curEnc[2] = '=';
                    curEnc[3] = '=';
                    break;
                case 2:
                    curEnc[3] = '=';
                    break;
            }
            for (int n = 0; n < 4; n++) {
                if (lineBreakCharFrequency > 0 && lineCount.intValue() == lineBreakCharFrequency) {
                    os.write('\n');
                    lineCount.set(1);
                } else {
                    lineCount.incrementAndGet();
                }
                os.write(curEnc[n]);
            }
        }
    }

    /**
     * Encode the bytes from input stream as base64 encoded string to outputstream
     * 
     * @param is
     * @param os
     * @throws IOException 
     */
    public void encode(InputStream is, OutputStream os) throws IOException {
        AtomicInteger lineCount = new AtomicInteger(0);
        int nIn = 0;
        int[] inbuf = new int[3];
        int n = 0;
        do {
            n = is.read();
            if (n != -1) {
                inbuf[nIn++] = n;
                if (nIn == 3) {
                    minorEncode(lineCount, 3, inbuf, os);
                    nIn = 0;
                }
            }
        } while (n != -1);
        if (nIn > 0) {
            minorEncode(lineCount, nIn, inbuf, os);
        }
        os.flush();
    }

}
