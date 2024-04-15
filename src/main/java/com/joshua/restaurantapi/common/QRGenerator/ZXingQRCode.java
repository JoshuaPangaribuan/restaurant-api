package com.joshua.restaurantapi.common.QRGenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.Cleanup;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Data
@Component
public class ZXingQRCode implements QRGenerator {

    private Path PATH;

    public ZXingQRCode()throws Exception{
        String path = System.getProperty("user.dir") + "/QRCode";
        this.PATH = Paths.get(path);

        if (!Files.exists(this.PATH)){
            Files.createDirectories(this.PATH);
        }
    }
    @Override
    public String generateQRCode(String text, int width, int height, String fileName) throws Exception {
        QRCodeWriter qrWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path finalPath = this.PATH.resolve(fileName + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", finalPath);

        byte[] finalPathBytes = finalPath.toString().getBytes(StandardCharsets.UTF_8);

        return Base64.getEncoder().encodeToString(finalPathBytes);
    }

    @Override
    public byte[] viewQRCode(String qrPath) throws Exception {
        byte[] decodeBytes = Base64.getDecoder().decode(qrPath);
        String decodedPath = new String(decodeBytes, StandardCharsets.UTF_8);

        BufferedImage bufferedImage = ImageIO.read(new File(decodedPath));
        @Cleanup ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);


        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public boolean deleteQRCode(String qrPath) throws Exception {
        String decodedPath = this.decodePath(qrPath);
        Path path = Paths.get(decodedPath);
        return Files.deleteIfExists(path);
    }

    @Override
    public boolean deleteBulkQRCode(String[] qrPaths) throws Exception {
        boolean isDeleted = true;
        for (String qrPath : qrPaths){
            isDeleted = this.deleteQRCode(qrPath);
        }
        return isDeleted;
    }

    private String decodePath(String qrPath){
        byte[] decodeBytes = Base64.getDecoder().decode(qrPath);
        return new String(decodeBytes, StandardCharsets.UTF_8);
    }
}
