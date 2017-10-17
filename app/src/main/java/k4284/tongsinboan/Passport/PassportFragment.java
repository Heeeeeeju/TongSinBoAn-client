package k4284.tongsinboan.Passport;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.DecimalFormat;
import java.util.Random;

import k4284.tongsinboan.R;

public class PassportFragment extends Fragment {

    public final static int RE_GENERATE_TIME = 30;
    private Handler timerHandler;

    public PassportFragment()
    {
        timerHandler = new Handler();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_passport, container, false);

        final ImageView qrCodeView = view.findViewById(R.id.passport_qr_code);
        final TextView textRemainTime = view.findViewById(R.id.passport_remain_time);

        Button buttonCreateQrCode = view.findViewById(R.id.passport_create_qr_code);
        buttonCreateQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerateQrCode(qrCodeView, textRemainTime);
            }
        });

        return view;
    }

    public void GenerateQrCode(ImageView qrCodeView, TextView textRemainTime)
    {
        DeleteQrCode();
        UpdateRemainTime(RE_GENERATE_TIME, textRemainTime);
        try {
            String data = GeneratePassData();
            Bitmap bitmap = EncodeAsBitmap(data);
            if (bitmap != null) {
                qrCodeView.setImageBitmap(bitmap);
                GenerateTimer(RE_GENERATE_TIME, textRemainTime);
            } else {
                GenerateTimer(0, textRemainTime);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void DeleteQrCode()
    {
        // TODO : 서버와 통신해서 기존에 생성한 데이터 삭제
        timerHandler.removeCallbacksAndMessages(null);
        Log.d("Passport", "DeleteQrCode()");
    }

    private String GeneratePassData()
    {
        // TODO : 암호화된 데이터 생성 및 서버 전송
        Log.d("Passport", "GenereatePassData");
        Random random = new Random();
        String data = String.valueOf(random.nextInt());
        return data;
    }

    private Bitmap EncodeAsBitmap(String data) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    private void GenerateTimer(final int remainTime, final TextView textRemainTime)
    {
        if (remainTime < 0) {
            DeleteQrCode();
            GenerateTimer(RE_GENERATE_TIME, textRemainTime);
        } else {
            WaitOneSecond(remainTime, textRemainTime);
        }
    }

    private void WaitOneSecond(final int remainTime, final TextView textRemainTime)
    {
        UpdateRemainTime(remainTime, textRemainTime);
        timerHandler.postDelayed(new Runnable(){
            @Override
            public void run() {
                GenerateTimer(remainTime - 1, textRemainTime);
            }
        }, 1000);
    }

    public void UpdateRemainTime(int remainTime, TextView textRemainTime)
    {
        DecimalFormat timeFormat = new DecimalFormat("00");
        String formattedMinute = timeFormat.format(remainTime / 60);
        String formattedSecond = timeFormat.format(remainTime % 60);
        String formattedTime = formattedMinute + ":" + formattedSecond;
        textRemainTime.setText(formattedTime);
    }
}
