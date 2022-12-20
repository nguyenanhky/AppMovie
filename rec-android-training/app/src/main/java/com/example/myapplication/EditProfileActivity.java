package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityEditProfileBinding;
import com.example.myapplication.databinding.DialogSelectAvaBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {

    private static Pattern pattern;
    SimpleDateFormat simpleDateFormat;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9]+[A-Za-z0-9]*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)$";

    private ActivityEditProfileBinding binding;
    private final String MALE = "Male";
    private final String FEMALE = "Female";
    private final int MY_CAMERA_REQUEST_CODE = 554;

    Uri photoURI = null;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != Activity.RESULT_OK) {
                    photoURI = Uri.parse(SharedPreferences.getAvatar(this));
                }
                Picasso.with(this).load(photoURI).transform(new CircleTransform()).error(R.drawable.bg_img_ava).into(binding.imgAva);
            });

    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        photoURI = intent.getData();
                        Picasso.with(this).load(photoURI).transform(new CircleTransform()).error(R.drawable.bg_img_ava).into(binding.imgAva);
                    }
                } else {
                    photoURI = Uri.parse(SharedPreferences.getAvatar(this));
                    Picasso.with(this).load(photoURI).transform(new CircleTransform()).error(R.drawable.bg_img_ava).into(binding.imgAva);
                }
            });

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pattern = Pattern.compile(EMAIL_REGEX);
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        showProfile();

        binding.btnBirthday.setOnClickListener(view -> showDatePicker());
        binding.btnDone.setOnClickListener(view -> {
            updateProfile();
        });
        binding.btnCancel.setOnClickListener(view -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        binding.imgAva.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            DialogSelectAvaBinding dialogBinding = DialogSelectAvaBinding.inflate(getLayoutInflater());
            dialog.setContentView(dialogBinding.getRoot());
            dialogBinding.txtCamera.setOnClickListener(view1 -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri cameraImageUri = getOutputMediaFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                    cameraActivityResultLauncher.launch(intent);
                }
                dialog.dismiss();
            });
            dialogBinding.txtGallery.setOnClickListener(view1 -> {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                galleryActivityResultLauncher.launch(intent);
                dialog.dismiss();
            });
            dialog.show();
        });

    }

    private void showProfile() {
        binding.editTextName.setText(SharedPreferences.getName(EditProfileActivity.this));
        String birthday = SharedPreferences.getBirthday(EditProfileActivity.this);
        if (birthday.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            String now = simpleDateFormat.format(calendar.getTime());
            binding.btnBirthday.setText(now);
        } else {
            binding.btnBirthday.setText(birthday);
        }
        binding.editTextMail.setText(SharedPreferences.getMail(EditProfileActivity.this));
        if (SharedPreferences.getSex(EditProfileActivity.this).equals(MALE))
            binding.radioMale.setChecked(true);
        else
            binding.radioFemale.setChecked(true);

        photoURI = Uri.parse(SharedPreferences.getAvatar(this));
        Picasso.with(this).load(photoURI).transform(new CircleTransform()).error(R.drawable.bg_img_ava).into(binding.imgAva);
    }

    private void updateProfile() {

        String name = binding.editTextName.getText().toString().trim();
        String mail = binding.editTextMail.getText().toString().trim();
        String birthday = binding.btnBirthday.getText().toString().trim();
        String sex = "";
        if (name.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, R.string.name_blank, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkMail(mail)) {
            return;
        }
        if (binding.radioFemale.isChecked())
            sex = FEMALE;
        else
            sex = MALE;

        SharedPreferences.saveName(EditProfileActivity.this, name);
        SharedPreferences.saveMail(EditProfileActivity.this, mail);
        SharedPreferences.saveSex(EditProfileActivity.this, sex);
        SharedPreferences.saveBirthday(EditProfileActivity.this, birthday);
        SharedPreferences.saveAvatar(EditProfileActivity.this, String.valueOf(photoURI));
        setResult(Activity.RESULT_OK);
        finish();
    }

    private boolean checkMail(String mail) {
        if (binding.editTextMail.getText().toString().trim().isEmpty()) {
            Toast.makeText(EditProfileActivity.this, R.string.email_blank, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!pattern.matcher(mail).matches()) {
            Toast.makeText(EditProfileActivity.this, R.string.email_wrong_format, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        Date date;
        try {
            date = simpleDateFormat.parse(binding.btnBirthday.getText().toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
            date = calendar.getTime();
        }

        if (date != null) {
            calendar.setTime(date);
        }
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH) + 1;
        int selectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year, monthOfYear, dayOfMonth) -> {
                    binding.btnBirthday.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
                }, selectedYear, selectedMonth, selectedDayOfMonth);

        datePickerDialog.show();
    }

    private Uri getOutputMediaFileUri() {
        photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getOutputMediaFile());
        return photoURI;
    }

    private File getOutputMediaFile() {

        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        String fileName = "Avatar_" + Calendar.getInstance().getTimeInMillis();
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName + ".png");

        return mediaFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri cameraImageUri = getOutputMediaFileUri();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                cameraActivityResultLauncher.launch(intent);
            }
        }
    }
}

class CircleTransform implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        canvas.rotate(90);
        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}


