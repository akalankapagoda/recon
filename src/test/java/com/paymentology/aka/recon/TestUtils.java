package com.paymentology.aka.recon;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public final class TestUtils {

    private static MultipartFile multipartFile;
    private static String line1;
    private static int recordsCount;

    static {
        StringBuilder stringBuilder = new StringBuilder();

        line1 = "ProfileName,TransactionDate,TransactionAmount,TransactionNarrative,TransactionDescription,TransactionID,TransactionType,WalletReference";

        stringBuilder.append(line1).append("\n");
        stringBuilder.append("Card Campaign,2014-01-11 22:27:44,-20000,*MOLEPS ATM25             MOLEPOLOLE    BW,DEDUCT,0584011808649511,1,P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5,\n");
        stringBuilder.append("Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,\n");
        stringBuilder.append("Card Campaign,2014-01-11 23:28:11,-5000,CAPITAL BANK              MOGODITSHANE  BW,DEDUCT,0464011844938429,1,P_NzI0NjE1NzhfMTM4NzE4ODExOC43NTYy,\n");

        byte[] content = stringBuilder.toString().getBytes();

        String filename = "sample.csv";

        multipartFile = new MockMultipartFile(filename,
                filename, "text/plain", content);

        recordsCount = 3;
    }

    public static final MultipartFile getTestDataFile() {
        return multipartFile;
    }

    public static final String getLine1() {
        return line1;
    }

    public static final int getRecordsCount() {
        return recordsCount;
    }
}
