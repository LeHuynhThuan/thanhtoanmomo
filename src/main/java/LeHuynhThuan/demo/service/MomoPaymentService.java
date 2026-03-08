package LeHuynhThuan.demo.service;

import LeHuynhThuan.demo.entity.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MomoPaymentService {

    private static final Logger log = LoggerFactory.getLogger(MomoPaymentService.class);
    private static final long MIN_AMOUNT_VND = 10_000;

    @Value("${momo.endpoint}")
    private String endpoint;

    @Value("${momo.partner-code}")
    private String partnerCode;

    @Value("${momo.access-key}")
    private String accessKey;

    @Value("${momo.secret-key}")
    private String secretKey;

    @Value("${momo.redirect-url}")
    private String redirectUrl;

    @Value("${momo.ipn-url}")
    private String ipnUrl;

    @Value("${momo.request-type:initiate}")
    private String requestType;

    /**
     * Tạo yêu cầu thanh toán MoMo và trả về payUrl để redirect người dùng.
     */
    public String createPayment(Invoice invoice) {
        long amount = Math.round(invoice.getDisplayFinalAmount());
        if (amount < MIN_AMOUNT_VND) {
            throw new IllegalArgumentException("Số tiền thanh toán tối thiểu qua MoMo là " + MIN_AMOUNT_VND + " VND.");
        }
        String orderId = invoice.getId();
        String requestId = UUID.randomUUID().toString();
        String orderInfo = "Thanh toán đơn hàng " + orderId;
        String extraData = "";

        String rawSignature = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + redirectUrl +
                "&requestId=" + requestId +
                "&requestType=" + requestType;

        String signature = hmacSHA256(rawSignature, secretKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", partnerCode);
        requestBody.put("partnerName", "BookStore");
        requestBody.put("storeId", "BookStore");
        requestBody.put("requestId", requestId);
        requestBody.put("amount", String.valueOf(amount));
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", orderInfo);
        requestBody.put("redirectUrl", redirectUrl);
        requestBody.put("ipnUrl", ipnUrl);
        requestBody.put("lang", "vi");
        requestBody.put("extraData", extraData);
        requestBody.put("requestType", requestType);
        requestBody.put("signature", signature);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(endpoint, requestBody, Map.class);
            Map<?, ?> body = response.getBody();
            if (body != null) {
                Object payUrl = body.get("payUrl");
                if (payUrl != null) {
                    return payUrl.toString();
                }
                Object resultCode = body.get("resultCode");
                Object message = body.get("message");
                if (resultCode != null && !"0".equals(String.valueOf(resultCode))) {
                    throw new IllegalStateException("MoMo trả lỗi: " + (message != null ? message : resultCode));
                }
            }
        } catch (HttpStatusCodeException e) {
            log.error("MoMo API lỗi {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            String msg = "MoMo từ chối yêu cầu. Kiểm tra partnerCode, accessKey, secretKey tại business.momo.vn. " + e.getMessage();
            throw new IllegalStateException(msg, e);
        }
        throw new IllegalStateException("Không tạo được liên kết thanh toán MoMo");
    }

    private String hmacSHA256(String data, String key) {
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secretKeySpec);
            byte[] hash = hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo chữ ký HMAC SHA256 cho MoMo", e);
        }
    }
}

