package com.joinjoy.service;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.joinjoy.model.bean.AcSignForm;

@Service
public class QRCodeService {

	@Autowired
	Storage storage;
	@Autowired
	AcSignFormService asfserv;

	// 透過三個字串生成的UUID來產生QRCode並上傳到Google Cloud Storage
	public void generateAndUploadQRCodeImage(String asfName, String asfEmail, String asfid) throws Exception {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		// ticketIdentifier是產生的UUID也是檔案名稱
		String ticketIdentifier = this.generateTicketIdentifier(asfName, asfEmail, asfid);
		BitMatrix bitMatrix = qrCodeWriter.encode(ticketIdentifier, BarcodeFormat.QR_CODE, 250, 250);

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
			byte[] qrCodeData = outputStream.toByteArray();

			// 上傳到 Google Cloud Storage
			BlobInfo blobInfo = BlobInfo.newBuilder("joy-bucket-12", ticketIdentifier).setContentType("image/png")
					.build();

			storage.create(blobInfo, qrCodeData);
		}
	}

	// 到gcs上拿QRcode返回(後續顯示到前端)
	public ResponseEntity<byte[]> getQRCodeImage(String asfName, String asfEmail, String asfid) {
		String ticketIdentifier = this.generateTicketIdentifier(asfName, asfEmail, asfid);
		Blob blob = storage.get(BlobId.of("joy-bucket-12", ticketIdentifier));
		if (blob == null) {
			return ResponseEntity.notFound().build();
		}
		byte[] content = blob.getContent();
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG) // 確保設置正確的內容類型
				.body(content);
	}

	// 接取報名者姓名 信箱 asfid來生成UUID
	public String generateTicketIdentifier(String asfName, String asfEmail, String asfid) {
		try {
			String source = String.valueOf(asfName) + String.valueOf(asfEmail) + String.valueOf(asfid);
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(source.getBytes("UTF-8"));
			return UUID.nameUUIDFromBytes(hash).toString();
		} catch (Exception e) {
			throw new RuntimeException("Error generating UUID", e);
		}
	}

	// 透過報名者姓名信箱asfid生成URL
	public String generateURL(String AsfName, String AsfEmail, String Asfid) {
		String hashed = sha256(AsfName + AsfEmail + Asfid);
		AcSignForm byAsfid = asfserv.findByAsfid(Integer.valueOf(Asfid));
		byAsfid.setAsfHash(hashed);
		asfserv.saveAcSignForm(byAsfid);
		// 因為要寄HTML靜態出去 所以是寫絕對路徑，要用http還是https再看看
		return "https://joinjoy.fun/validate?code=" + hashed;
	}

	// 把三個字串轉換成sha256哈西碼字串作為URL後綴
	private String sha256(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
			BigInteger number = new BigInteger(1, hash);
			StringBuilder hexString = new StringBuilder(number.toString(16));
			while (hexString.length() < 32) {
				hexString.insert(0, '0');
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException("Could not hash input", e);
		}
	}

	// 比對掃描到的QRcode和資料庫的QRcode是否相同 相同就改變這張票的狀態成已驗過票
	public String validateQRCodeAndChangeStatus(String QRcode) {
		String upperCode = QRcode.toUpperCase();
		AcSignForm byasfQRcode = asfserv.findByasfQRcode(upperCode);
	    if (byasfQRcode == null) {
	        return "驗票失敗，無此票券";
	    }
	    
	    if (byasfQRcode.getAsfQRcode().equals(upperCode)) {
	        if (byasfQRcode.getAsfVerified() == 0) {
	            byasfQRcode.setAsfVerified(1);
	            asfserv.saveAcSignForm(byasfQRcode);
	            return "驗票成功，請進場";
	        } else {
	            return "此票券已進場過了！";
	        }
	    }
	    
	    return "驗票失敗，無此票券";
	}

}
