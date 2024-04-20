package com.joshua.restaurantapi.common.QRGenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.joshua.restaurantapi.configuration.MinioConfiguration;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;
import lombok.Cleanup;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Data
@Component
public class ZXingQRCode implements QRGenerator {
    private MinioClient qrBucket;
    private String bucketName;
    public ZXingQRCode(MinioClient minioClient, MinioConfiguration minioConfiguration)throws Exception{
        this.qrBucket = minioClient;
        this.bucketName = minioConfiguration.getQrBucket();
    }
    @Override
    public String generateQRCode(String text, int width, int height, String fileName) throws Exception {
        QRCodeWriter qrWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // Create a temporary file
        Path tempPath = Files.createTempFile(fileName, ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", tempPath);

        // Upload the file to Minio
        qrBucket.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName + ".png")
                        .filename(tempPath.toString())
                        .build()
        );

        // Get presigned URL
        String url = qrBucket.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(fileName + ".png")
                        .build()
        );

        // Delete the temporary file
        Files.delete(tempPath);

        byte[] finalPathBytes = url.getBytes(StandardCharsets.UTF_8);

        return Base64.getEncoder().encodeToString(finalPathBytes);
    }

    @Override
    public byte[] viewQRCode(String qrPath) throws Exception {
        byte[] decodeBytes = Base64.getDecoder().decode(qrPath);
        String decodeURL = new String(decodeBytes, StandardCharsets.UTF_8);

        URL url = new URL(decodeURL);

        BufferedImage bufferedImage = ImageIO.read(url);
        @Cleanup ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);


        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public boolean deleteQRCode(String qrPath) {
        try{
            byte[] decodeBytes = Base64.getDecoder().decode(qrPath);
            String decodeURL = new String(decodeBytes, StandardCharsets.UTF_8);

            URL url = new URL(decodeURL);
            String fileName = Paths.get(url.getPath()).getFileName().toString();

            qrBucket.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e){
            System.out.println("Error deleting QR Code: " + e.getMessage());
            return false;
        }

        return true;
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
