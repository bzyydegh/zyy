package cn.edu.gdaibzyy.qrcodezxing.decode;

import android.content.Intent;
import android.net.Uri;

import com.google.zxing.BarcodeFormat;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

final class DecodeFormatManager {

  private static final Pattern COMMA_PATTERN = Pattern.compile(",");

  static final Vector<BarcodeFormat> PRODUCT_FORMATS;
  static final Vector<BarcodeFormat> ONE_D_FORMATS;
  static final Vector<BarcodeFormat> QR_CODE_FORMATS;
  static final Vector<BarcodeFormat> DATA_MATRIX_FORMATS;
  static {
    PRODUCT_FORMATS = new Vector<BarcodeFormat>(5);
    PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
    PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
    PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
    PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
    PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
    ONE_D_FORMATS = new Vector<BarcodeFormat>(PRODUCT_FORMATS.size() + 4);
    ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
    ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
    ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
    ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
    ONE_D_FORMATS.add(BarcodeFormat.ITF);
    QR_CODE_FORMATS = new Vector<BarcodeFormat>(1);
    QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
    DATA_MATRIX_FORMATS = new Vector<BarcodeFormat>(1);
    DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);
  }

  private DecodeFormatManager() {}

  static Vector<BarcodeFormat> parseDecodeFormats(Intent intent) {
    List<String> scanFormats = null;
    String scanFormatsString = intent.getStringExtra(cn.edu.gdaibzyy.qrcodezxing.decode.Intents.Scan.SCAN_FORMATS);
    if (scanFormatsString != null) {
      scanFormats = Arrays.asList(COMMA_PATTERN.split(scanFormatsString));
    }
    return parseDecodeFormats(scanFormats, intent.getStringExtra(cn.edu.gdaibzyy.qrcodezxing.decode.Intents.Scan.MODE));
  }

  static Vector<BarcodeFormat> parseDecodeFormats(Uri inputUri) {
    List<String> formats = inputUri.getQueryParameters(cn.edu.gdaibzyy.qrcodezxing.decode.Intents.Scan.SCAN_FORMATS);
    if (formats != null && formats.size() == 1 && formats.get(0) != null){
      formats = Arrays.asList(COMMA_PATTERN.split(formats.get(0)));
    }
    return parseDecodeFormats(formats, inputUri.getQueryParameter(cn.edu.gdaibzyy.qrcodezxing.decode.Intents.Scan.MODE));
  }

  private static Vector<BarcodeFormat> parseDecodeFormats(Iterable<String> scanFormats,
                                                          String decodeMode) {
    if (scanFormats != null) {
      Vector<BarcodeFormat> formats = new Vector<BarcodeFormat>();
      try {
        for (String format : scanFormats) {
          formats.add(BarcodeFormat.valueOf(format));
        }
        return formats;
      } catch (IllegalArgumentException iae) {
        // ignore it then
      }
    }
    if (decodeMode != null) {
      if (cn.edu.gdaibzyy.qrcodezxing.decode.Intents.Scan.PRODUCT_MODE.equals(decodeMode)) {
        return PRODUCT_FORMATS;
      }
      if (cn.edu.gdaibzyy.qrcodezxing.decode.Intents.Scan.QR_CODE_MODE.equals(decodeMode)) {
        return QR_CODE_FORMATS;
      }
      if (cn.edu.gdaibzyy.qrcodezxing.decode.Intents.Scan.DATA_MATRIX_MODE.equals(decodeMode)) {
        return DATA_MATRIX_FORMATS;
      }
      if (cn.edu.gdaibzyy.qrcodezxing.decode.Intents.Scan.ONE_D_MODE.equals(decodeMode)) {
        return ONE_D_FORMATS;
      }
    }
    return null;
  }

}
