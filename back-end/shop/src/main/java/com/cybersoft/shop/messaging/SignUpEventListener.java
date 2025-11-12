package com.cybersoft.shop.messaging;

import com.cybersoft.shop.messaging.event.SignUpSuccessEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpEventListener {

    private final JavaMailSender mailSender;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${spring.kafka.topics.user-signup-success}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onSignUp(String payload) throws Exception {
        SignUpSuccessEvent event = objectMapper.readValue(payload, SignUpSuccessEvent.class);

        var mime = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mime, true, "UTF-8");

        helper.setTo(event.getEmail());
        helper.setSubject("Xác thực tài khoản Shoes Shop của bạn");

        String name = (event.getUserName() == null || event.getUserName().isBlank())
                ? "bạn"
                : event.getUserName();

        String html = """
        <html>
          <head>
            <meta charset="UTF-8">
            <style>
              body { font-family: Arial, sans-serif; color:#222; }
              .card { max-width:600px; margin:auto; border:1px solid #eee; border-radius:10px; overflow:hidden; }
              .header { background:#111827; color:#fff; padding:16px 20px; font-size:20px; }
              .content { padding:20px; line-height:1.6; }
              .code-box {
                display:inline-block;
                padding:12px 20px;
                font-size:24px;
                font-weight:bold;
                letter-spacing:4px;
                border-radius:8px;
                border:1px dashed #4B5563;
                margin:16px 0;
              }
              .footer { padding:16px 20px; font-size:12px; color:#6B7280; border-top:1px solid #eee;}
            </style>
          </head>
          <body>
            <div class="card">
              <div class="header">Xác thực email</div>
              <div class="content">
                <p>Xin chào %s,</p>
                <p>Cảm ơn bạn đã đăng ký tài khoản tại <b>Shoes Shop</b>.</p>
                <p>Để hoàn tất đăng ký, hãy nhập mã xác thực bên dưới vào màn hình xác thực trên website:</p>
                <div class="code-box">%s</div>
                <p>Mã này có hiệu lực trong <b>5 phút</b>. Nếu bạn không thực hiện đăng ký, vui lòng bỏ qua email này.</p>
              </div>
              <div class="footer">
                &copy; %d Shoes Shop. Vui lòng không trả lời email này.
              </div>
            </div>
          </body>
        </html>
        """.formatted(name, event.getCode(), java.time.Year.now().getValue());

        helper.setText(html, true);

        mailSender.send(mime);
        System.out.println("[MAIL] sent verification email to " + event.getEmail());
    }
}

