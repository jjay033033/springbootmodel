package top.lmoon.util;

public final class Base64 {
	/** */
	private static final int BASELENGTH = 255;
	static private final int BASELENGTH_128     = 128;
	/** */
	private static final int LOOKUPLENGTH = 64;

	/** */
	private static final int TWENTYFOURBITGROUP = 24;

	/** */
	private static final int EIGHTBIT = 8;

	/** */
	private static final int SIXTEENBIT = 16;

	/** */
	private static final int FOURBYTE = 4;

	/** The sign bit as an int */
	private static final int SIGN = -128;

	/** The padding character */
	private static final byte PAD = (byte) '=';

	/** The alphabet */
	private static final byte[] BASE64_ALPHABET = new byte[BASELENGTH];
	static private final boolean fDebug               = false;
	static final private byte[]  base64Alphabet       = new byte[BASELENGTH_128];	

	/** The lookup alphabet */
	private static final byte[] LOOKUP_BASE64_ALPHABET = new byte[LOOKUPLENGTH];
	static final private char[]  lookUpBase64Alphabet = new char[LOOKUPLENGTH];

	static {

		for (int i = 0; i < BASELENGTH; i++) {
			BASE64_ALPHABET[i] = -1;
		}
		for (int i = 'Z'; i >= 'A'; i--) {
			BASE64_ALPHABET[i] = (byte) (i - 'A');
		}
		for (int i = 'z'; i >= 'a'; i--) {
			BASE64_ALPHABET[i] = (byte) (i - 'a' + 26);
		}

		for (int i = '9'; i >= '0'; i--) {
			BASE64_ALPHABET[i] = (byte) (i - '0' + 52);
		}

		BASE64_ALPHABET['+'] = 62;
		BASE64_ALPHABET['/'] = 63;
		
		
        for (int i = 0; i < BASELENGTH_128; ++i) {
            base64Alphabet[i] = -1;
        }
        for (int i = 'Z'; i >= 'A'; i--) {
            base64Alphabet[i] = (byte) (i - 'A');
        }
        for (int i = 'z'; i >= 'a'; i--) {
            base64Alphabet[i] = (byte) (i - 'a' + 26);
        }

        for (int i = '9'; i >= '0'; i--) {
            base64Alphabet[i] = (byte) (i - '0' + 52);
        }

        base64Alphabet['+'] = 62;
        base64Alphabet['/'] = 63;
        

		for (int i = 0; i <= 25; i++) {
			LOOKUP_BASE64_ALPHABET[i] = (byte) ('A' + i);
		}

		for (int i = 26, j = 0; i <= 51; i++, j++) {
			LOOKUP_BASE64_ALPHABET[i] = (byte) ('a' + j);
		}

		for (int i = 52, j = 0; i <= 61; i++, j++) {
			LOOKUP_BASE64_ALPHABET[i] = (byte) ('0' + j);
		}
		LOOKUP_BASE64_ALPHABET[62] = (byte) '+';
		LOOKUP_BASE64_ALPHABET[63] = (byte) '/';

        for (int i = 0; i <= 25; i++) {
            lookUpBase64Alphabet[i] = (char) ('A' + i);
        }

        for (int i = 26, j = 0; i <= 51; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('a' + j);
        }

        for (int i = 52, j = 0; i <= 61; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('0' + j);
        }
        lookUpBase64Alphabet[62] = (char) '+';
        lookUpBase64Alphabet[63] = (char) '/';
        
	}

    private static boolean isWhiteSpace(char octect) {
        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
    }
    private static boolean isPad(char octect) {
        return (octect == PAD);
    }
    private static boolean isData(char octect) {
        return (octect < BASELENGTH_128 && base64Alphabet[octect] != -1);
    }
    
    /**
     * Encodes hex octects into Base64
     *
     * @param binaryData Array containing binaryData
     * @return Encoded Base64 array
     */
//    public static String encode(byte[] binaryData) {
//
//        if (binaryData == null) {
//            return null;
//        }
//
//        int lengthDataBits = binaryData.length * EIGHTBIT;
//        if (lengthDataBits == 0) {
//            return "";
//        }
//
//        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
//        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
//        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
//        char encodedData[] = null;
//
//        encodedData = new char[numberQuartet * 4];
//
//        byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;
//
//        int encodedIndex = 0;
//        int dataIndex = 0;
//        if (fDebug) {
//            System.out.println("number of triplets = " + numberTriplets);
//        }
//
//        for (int i = 0; i < numberTriplets; i++) {
//            b1 = binaryData[dataIndex++];
//            b2 = binaryData[dataIndex++];
//            b3 = binaryData[dataIndex++];
//
//            if (fDebug) {
//                System.out.println("b1= " + b1 + ", b2= " + b2 + ", b3= " + b3);
//            }
//
//            l = (byte) (b2 & 0x0f);
//            k = (byte) (b1 & 0x03);
//
//            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
//            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
//            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);
//
//            if (fDebug) {
//                System.out.println("val2 = " + val2);
//                System.out.println("k4   = " + (k << 4));
//                System.out.println("vak  = " + (val2 | (k << 4)));
//            }
//
//            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
//            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
//            encodedData[encodedIndex++] = lookUpBase64Alphabet[(l << 2) | val3];
//            encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3f];
//        }
//
//        // form integral number of 6-bit groups
//        if (fewerThan24bits == EIGHTBIT) {
//            b1 = binaryData[dataIndex];
//            k = (byte) (b1 & 0x03);
//            if (fDebug) {
//                System.out.println("b1=" + b1);
//                System.out.println("b1<<2 = " + (b1 >> 2));
//            }
//            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
//            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
//            encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
//            encodedData[encodedIndex++] = PAD;
//            encodedData[encodedIndex++] = PAD;
//        } else if (fewerThan24bits == SIXTEENBIT) {
//            b1 = binaryData[dataIndex];
//            b2 = binaryData[dataIndex + 1];
//            l = (byte) (b2 & 0x0f);
//            k = (byte) (b1 & 0x03);
//
//            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
//            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
//
//            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
//            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
//            encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
//            encodedData[encodedIndex++] = PAD;
//        }
//
//        return new String(encodedData);
//    }
    
	/**
	 * Return true if the specified octect is base64
	 * 
	 * @param octect
	 *            The octet to test.
	 * @return boolean True if the octect is base64.
	 */
	static boolean isBase64(byte octect) {
		// Should we ignore white space?
		return (octect == PAD || BASE64_ALPHABET[octect] != -1);
	}
	
	/**
	 * 编码
	 * @param source 源字符串
	 * @param charset 字符编码
	 * @return 编码结果
	 */
	public static String encode(String source, String charset)
	{
		try
		{
			byte []ret = encode(source.getBytes(charset));
			String str = new String(ret, charset);
			return str;
		}
		catch(Exception ex)
		{
			return "";
		}
	}

	/**
	 * Encodes hex octects into Base64
	 * 
	 * @param binaryData
	 *            Array containing binaryData
	 * @return Base64-encoded array
	 */
	public static byte[] encode(byte[] binaryData) {

		int lengthDataBits = binaryData.length * EIGHTBIT;
		int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
		int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
		byte encodedData[] = null;

		if (fewerThan24bits != 0) { // data not divisible by 24 bit
			encodedData = new byte[(numberTriplets + 1) * 4];
		} else { // 16 or 8 bit
			encodedData = new byte[numberTriplets * 4];
		}

		byte k = 0;
		byte l = 0;
		byte b1 = 0;
		byte b2 = 0;
		byte b3 = 0;
		int encodedIndex = 0;
		int dataIndex = 0;
		int i = 0;
		for (i = 0; i < numberTriplets; i++) {

			dataIndex = i * 3;
			b1 = binaryData[dataIndex];
			b2 = binaryData[dataIndex + 1];
			b3 = binaryData[dataIndex + 2];

			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			encodedIndex = i * 4;
			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);

			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
					: (byte) ((b2) >> 4 ^ 0xf0);
			byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6)
					: (byte) ((b3) >> 6 ^ 0xfc);

			encodedData[encodedIndex] = LOOKUP_BASE64_ALPHABET[val1];
			encodedData[encodedIndex + 1] = LOOKUP_BASE64_ALPHABET[val2
					| (k << 4)];
			encodedData[encodedIndex + 2] = LOOKUP_BASE64_ALPHABET[(l << 2)
					| val3];
			encodedData[encodedIndex + 3] = LOOKUP_BASE64_ALPHABET[b3 & 0x3f];
		}

		// form integral number of 6-bit groups
		dataIndex = i * 3;
		encodedIndex = i * 4;
		if (fewerThan24bits == EIGHTBIT) {
			b1 = binaryData[dataIndex];
			k = (byte) (b1 & 0x03);
			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);
			encodedData[encodedIndex] = LOOKUP_BASE64_ALPHABET[val1];
			encodedData[encodedIndex + 1] = LOOKUP_BASE64_ALPHABET[k << 4];
			encodedData[encodedIndex + 2] = PAD;
			encodedData[encodedIndex + 3] = PAD;
		} else if (fewerThan24bits == SIXTEENBIT) {
			b1 = binaryData[dataIndex];
			b2 = binaryData[dataIndex + 1];
			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
					: (byte) ((b1) >> 2 ^ 0xc0);
			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
					: (byte) ((b2) >> 4 ^ 0xf0);

			encodedData[encodedIndex] = LOOKUP_BASE64_ALPHABET[val1];
			encodedData[encodedIndex + 1] = LOOKUP_BASE64_ALPHABET[val2
					| (k << 4)];
			encodedData[encodedIndex + 2] = LOOKUP_BASE64_ALPHABET[l << 2];
			encodedData[encodedIndex + 3] = PAD;
		}
		return encodedData;
	}
	
	/**
	 * 解码
	 * @param source 源字符串
	 * @param charset 字符编码
	 * @return 解码结果
	 */
	public static String decode(String source, String charset)
	{
		try
		{
			byte []ret = decode(source.getBytes(charset));
			String str = new String(ret, charset);
			return str;
		}
		catch(Exception ex)
		{
			return "";
		}
	}

	/**
	 * Decodes Base64 data into octects
	 * 
	 * @param base64Data
	 *            byte array containing Base64 data
	 * @return Array containing decoded data.
	 */
	public static byte[] decode(byte[] base64Data) {
		// Should we throw away anything not in base64Data ?

		// handle the edge case, so we don't have to worry about it later
		if (base64Data.length == 0) {
			return new byte[0];
		}

		int numberQuadruple = base64Data.length / FOURBYTE;
		byte decodedData[] = null;
		byte b1 = 0, b2 = 0, b3 = 0, b4 = 0, marker0 = 0, marker1 = 0;

		int encodedIndex = 0;
		int dataIndex = 0;
		{
			// this block sizes the output array properly - rlw
			int lastData = base64Data.length;
			// ignore the '=' padding
			while (base64Data[lastData - 1] == PAD) {
				if (--lastData == 0) {
					return new byte[0];
				}
			}
			decodedData = new byte[lastData - numberQuadruple];
		}

		for (int i = 0; i < numberQuadruple; i++) {
			dataIndex = i * 4;
			marker0 = base64Data[dataIndex + 2];
			marker1 = base64Data[dataIndex + 3];

			b1 = BASE64_ALPHABET[base64Data[dataIndex]];
			b2 = BASE64_ALPHABET[base64Data[dataIndex + 1]];

			if (marker0 != PAD && marker1 != PAD) { // No PAD e.g 3cQl
				b3 = BASE64_ALPHABET[marker0];
				b4 = BASE64_ALPHABET[marker1];

				decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
				decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
				decodedData[encodedIndex + 2] = (byte) (b3 << 6 | b4);
			} else if (marker0 == PAD) { // Two PAD e.g. 3c[Pad][Pad]
				decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
			} else if (marker1 == PAD) { // One PAD e.g. 3cQ[Pad]
				b3 = BASE64_ALPHABET[marker0];
				decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
				decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
			}
			encodedIndex += 3;
		}
		return decodedData;
	}

	   /**
     * Decodes Base64 data into octects
     *
     * @param encoded string containing Base64 data
     * @return Array containind decoded data.
     */
    public static byte[] decode(String encoded) {

        if (encoded == null) {
            return null;
        }

        char[] base64Data = encoded.toCharArray();
        // remove white spaces
        int len = removeWhiteSpace(base64Data);

        if (len % FOURBYTE != 0) {
            return null;//should be divisible by four
        }

        int numberQuadruple = (len / FOURBYTE);

        if (numberQuadruple == 0) {
            return new byte[0];
        }

        byte decodedData[] = null;
        byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
        char d1 = 0, d2 = 0, d3 = 0, d4 = 0;

        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[(numberQuadruple) * 3];

        for (; i < numberQuadruple - 1; i++) {

            if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))
                || !isData((d3 = base64Data[dataIndex++]))
                || !isData((d4 = base64Data[dataIndex++]))) {
                return null;
            }//if found "no data" just return null

            b1 = base64Alphabet[d1];
            b2 = base64Alphabet[d2];
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];

            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }

        if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))) {
            return null;//if found "no data" just return null
        }

        b1 = base64Alphabet[d1];
        b2 = base64Alphabet[d2];

        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        if (!isData((d3)) || !isData((d4))) {//Check if they are PAD characters
            if (isPad(d3) && isPad(d4)) {
                if ((b2 & 0xf) != 0)//last 4 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            } else if (!isPad(d3) && isPad(d4)) {
                b3 = base64Alphabet[d3];
                if ((b3 & 0x3) != 0)//last 2 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            } else {
                return null;
            }
        } else { //No PAD e.g 3cQl
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);

        }

        return decodedData;
    }
    
    /**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     *
     * @param data  the byte array of base64 data (with WS)
     * @return      the new length
     */
    private static int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        }

        // count characters that's not whitespace
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                data[newSize++] = data[i];
            }
        }
        return newSize;
    }
}