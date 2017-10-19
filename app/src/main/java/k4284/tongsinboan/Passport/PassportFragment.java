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

import org.json.JSONObject;

import java.text.DecimalFormat;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;

public class PassportFragment extends Fragment {

    private Handler timerHandler;

    ImageView qrCodeView;
    TextView textRemainTime;

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

        qrCodeView = view.findViewById(R.id.passport_qr_code);
        textRemainTime = view.findViewById(R.id.passport_remain_time);

        Button buttonCreateQrCode = view.findViewById(R.id.passport_create_qr_code);
        buttonCreateQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassTokenRequest();
            }
        });

        return view;
    }

    public void PassTokenRequest()
    {
        new Thread() {
            public void run() {
                String requestName = "/member/token";
                JSONObject response = App.ServerRequest(App.REQUEST_GET, requestName);
                try {
                    boolean result = response.getBoolean("result");
                    if (result) {
                        JSONObject data = response.getJSONObject("data");
                        GenerateQrCode(data);
                    } else {
                        String errorMessage = response.getString("msg");
                        if (errorMessage.equals("authentication_required")) {
                            App.MakeToastMessage("로그인 되어있지 않습니다");
                        } else if (errorMessage.equals("member_token_create_failed")) {
                            App.MakeToastMessage("서버 오류로 생성에 실패했습니다");
                        }
                    }
                } catch (Exception e) {
                    Log.e("Passport", e.toString());
                }
            }
        }.start();
    }

    public void GenerateQrCode(final JSONObject data)
    {
        DeleteQrCode();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    int expireTime = data.getInt("expire");
                    UpdateRemainTime(expireTime);

                    String token = data.getString("token");
                    Log.d("Passport", token);
                    Bitmap bitmap = EncodeAsBitmap(token);
                    if (bitmap != null) {
                        qrCodeView.setImageBitmap(bitmap);
                        GenerateTimer(expireTime, textRemainTime);
                    } else {
                        App.MakeToastMessage("출입 코드 생성에 실패했습니다");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void DeleteQrCode()
    {
        timerHandler.removeCallbacksAndMessages(null);
        Log.d("Passport", "DeleteQrCode()");
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
            PassTokenRequest();
        } else {
            WaitOneSecond(remainTime);
        }
    }

    private void WaitOneSecond(final int remainTime)
    {
        UpdateRemainTime(remainTime);
        timerHandler.postDelayed(new Runnable(){
            @Override
            public void run() {
                GenerateTimer(remainTime - 1, textRemainTime);
            }
        }, 1000);
    }

    public void UpdateRemainTime(int remainTime)
    {
        DecimalFormat timeFormat = new DecimalFormat("00");
        String formattedMinute = timeFormat.format(remainTime / 60);
        String formattedSecond = timeFormat.format(remainTime % 60);
        String formattedTime = formattedMinute + ":" + formattedSecond;
        textRemainTime.setText(formattedTime);
    }
}
