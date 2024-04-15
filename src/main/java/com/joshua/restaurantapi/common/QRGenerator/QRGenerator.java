package com.joshua.restaurantapi.common.QRGenerator;

public interface QRGenerator {
    String generateQRCode(String text, int width, int height, String fileName) throws Exception;
    byte[] viewQRCode(String qrPath) throws Exception;

    boolean deleteQRCode(String qrPath) throws Exception;

    boolean deleteBulkQRCode(String[] qrPaths) throws Exception;
}
