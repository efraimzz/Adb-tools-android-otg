package com.adbusba;
 
import android.annotation.NonNull;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.adbusba.MainActivity;
import com.cgutman.adblib.AdbBase64;
import com.cgutman.adblib.AdbConnection;
import com.cgutman.adblib.AdbCrypto;
import com.cgutman.adblib.AdbStream;
import com.cgutman.adblib.UsbChannel;
import in.hridayan.ashell.Message;
import in.hridayan.ashell.config.Const;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static in.hridayan.ashell.Message.CONNECTING;
import static in.hridayan.ashell.Message.DEVICE_FOUND;
import static in.hridayan.ashell.Message.DEVICE_NOT_FOUND;
import static in.hridayan.ashell.Message.FLASHING;
import static in.hridayan.ashell.Message.INSTALLING_PROGRESS;
import static in.hridayan.ashell.Message.PUSH_PART;
import java.io.FileNotFoundException;
import android.hardware.usb.UsbEndpoint;
import in.hridayan.ashell.utils.OtgUtils;
import android.os.Build;
import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;



public class MainActivity extends Activity implements TextView.OnEditorActionListener, View.OnKeyListener {
	
	boolean logdev=true;
	private Handler handler;
	MainActivity mcon=this;
    private UsbDevice mDevice;
    private TextView tvStatus;//,logs;
    private ImageView usb_icon;
    private AdbCrypto adbCrypto;
    private AdbConnection adbConnection;
    private UsbManager mManager;
    //private RelativeLayout terminalView;
    private LinearLayout checkContainer;
    //private EditText edCommand;
    //private Button btnRun;
    //private ScrollView scrollView;
    private String user = null;
	private boolean doubleBackToExitPressedOnce = false;
    private AdbStream stream;
    EditText edapp;
    EditText edclient;
    EditText edcmd;
    EditText edhost;
    Button mainstall;
    Button mapull;
    Button mapush;
    Button maruncmd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		mpermission("android.permission.WRITE_EXTERNAL_STORAGE");
		if (logdev) {
			log log = new log();
			log.setDaemon(true);
			log.start();
		}
        setContentView(R.layout.activity_main);
		this.tvStatus = (TextView) findViewById(2131427330);
        usb_icon = (ImageView) findViewById(R.id.usb_icon);
       
        usb_icon.setImageResource(R.drawable.ic_usb);
        checkContainer = (LinearLayout) findViewById(R.id.checkContainer);
        edhost = (EditText) findViewById(R.id.maedhost);
        edclient = (EditText) findViewById(R.id.maedclient);
        mapull = (Button) findViewById(R.id.mabupull);
       
        mapull.setVisibility(View.GONE);
        mapush = (Button) findViewById(R.id.mabupush);
       
        mapush.setVisibility(View.GONE);
        this.edapp = (EditText) findViewById(R.id.maedapp);
        mainstall = (Button) findViewById(R.id.mabuinstall);
       
        mainstall.setVisibility(View.GONE);
        edcmd = (EditText) findViewById(R.id.maedcmd);
        maruncmd = (Button) findViewById(R.id.maburuncmd);
       
        maruncmd.setVisibility(View.GONE);
        this.mapull.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    new Thread(){public void run(){
                        
                    
                    File fil=new File(edhost.getText().toString());
                    if(fil.getParentFile().canRead()&&!edclient.getText().toString().equals(""))
                        new OtgUtils.PullexNew(adbConnection, fil,edclient.getText().toString()).execute(handler);
                        }}.start();
                }
            });
        this.mapush.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    File fil=new File(edhost.getText().toString());
                    if(fil.exists()&&fil.canRead()&&!edclient.getText().toString().equals(""))
                    new OtgUtils.Pushnew(mcon,adbConnection, fil,edclient.getText().toString(),false).execute(handler);
                }
            });
        this.mainstall.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    new Thread(){public void run(){
                    try{
                        //new OtgUtils.Install(adbConnection,"",0).execute(handler);
                        String mfileName=edapp.getText().toString();
                        if(!mfileName.equals("")){
                            if(new File(mfileName).canRead()){
                                new OtgUtils.Pushnew(mcon,adbConnection,new File(mfileName),"/data/local/tmp/tmp.apk",true).execute(handler);
                                //continue install in push
                            }else{
                                LogUtil.logToFile("notread");
                            }
                        }else{
                            LogUtil.logToFile("eq...");
                        }
                    } catch (Exception e) {
                        LogUtil.logToFile(e.toString());
                    }
                        }}.start();
                    /*
                    try{
                        //8 data
                        //4096*16 no data


                        //skip8
                        //write 4088
                        //write 4096 *15

                        //write 8
                        //end write 4096*16...
                        //skip 8
                        //write 4080
                        //write 4096 *15

                        //write 16
                        //end write 4096*16...
                        //skip 8
                        //write 4072
                        //write 4096 *15


                        //if i3 is more then

                        byte[] bArr = new byte[4096];
                        int ws=-1;
                        FileInputStream fis = new FileInputStream("/storage/emulated/0/bu.apk");
                        FileOutputStream fos = new FileOutputStream("/storage/emulated/0/bbu.apk");
                        int i2=1;
                        int i3=0;
                        boolean skip8=false;
                        int len=0;
                        
                        while((ws=fis.read(bArr))!=-1){//reading 4096
                            if(i2==1){//1 or 17 (17eq1...) 
                                //any 17 add 8 - first 17 is 8, second 17 is 16
                                //skip8=true;
                            }else if(i2==17){

                                skip8=true;
                                i2=1;//17 eq 1
                                i3++;
                                log.a("i3="+i3);
                                //if(i3==17)i3=1;
                                len=0+(i3*8);
                            }else{
                                skip8=false;
                            }
                            if(skip8){
                                if(i3!=1){
                                    //no need tonwrite from 0 to 0
                                    fos.write(bArr, 0, ((8*i3)-8));//second write 8
                                    log.a("sum1="+((8*i3)-8));
                                    len+=((8*i3)-8);
                                    log.a(len);
                                }
                                // if(i3==1){
                                //   fos.write(bArr, (8*i3), bArr.length-((8*i3)));
                                //    log.a("fsum2="+(bArr.length-((8*i3))));
                                //}else{

                                fos.write(bArr, (8*i3), bArr.length-(8*i3));//first skip 8
                                log.a("sum2="+(bArr.length-((8*i3))));
                                len+=(bArr.length-((8*i3)));
                                log.a(len);
                                //  }
                                //fos.write(bArr, bArr.length-(8*i3)+8, (8*i3)-8);
                                //log.a("sum3="+((8*i3)-8));
                            }else{
                                fos.write(bArr, 0, ws);
                                log.a("sum="+bArr.length);
                                len+=(bArr.length);
                                log.a(len);
                            }
                            i2++;
                            //fos.write(bArr);

                        }
                    }catch(Exception e){
                        log.a(e);
                    }*/
                }
            });
        this.maruncmd.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    customcommand(edcmd.getText().toString());
                    /*
                    try{
                        //8 data
                        //4096*16 no data
                        
                        
                        //skip8
                        //write 4088
                        //write 4096 *15
                        
                        //write 8
                        //end write 4096*16...
                        //skip 8
                        //write 4080
                        //write 4096 *15
                        
                        //write 16
                        //end write 4096*16...
                        //skip 8
                        //write 4072
                        //write 4096 *15
                        
                        
                        //if i3 is more then
                        
                    byte[] bArr = new byte[4096];
                    int ws=-1;
                        FileInputStream fis = new FileInputStream("/storage/emulated/0/bu.apk");
                        FileOutputStream fos = new FileOutputStream("/storage/emulated/0/bbu.apk");
                    int i2=1;
                    int i3=1;
                    boolean skip8=false;
                    int len=0;
                    
                        while((ws=fis.read(bArr))!=-1){//reading 4096
                            if(i2==1){//1 or 17 (17eq1...) 
                                //any 17 add 8 - first 17 is 8, second 17 is 16
                                skip8=true;
                            }else if(i2==17){

                                skip8=true;
                                i2=1;//17 eq 1
                                i3++;
                                log.a("i3="+i3);
                                if(i3==513)i3=1;
                                len=0+(i3*8);
                            }else{
                                skip8=false;
                            }
                            if(skip8){
                                if(i3!=1){
                                    //no need tonwrite from 0 to 0
                                    fos.write(bArr, 0, ((8*i3)-8));//second write 8
                                    log.a("sum1="+((8*i3)-8));
                                    len+=((8*i3)-8);
                                    log.a(len);
                                }
                                // if(i3==1){
                                //   fos.write(bArr, (8*i3), bArr.length-((8*i3)));
                                //    log.a("fsum2="+(bArr.length-((8*i3))));
                                //}else{
                                    
                                fos.write(bArr, (8*i3), bArr.length-(8*i3));//first skip 8
                                log.a("sum2="+(bArr.length-((8*i3))));
                                len+=(bArr.length-((8*i3)));
                                log.a(len);
                                //  }
                                //fos.write(bArr, bArr.length-(8*i3)+8, (8*i3)-8);
                                //log.a("sum3="+((8*i3)-8));
                            }else{
                                fos.write(bArr, 0, ws);
                                log.a("sum="+bArr.length);
                                len+=(bArr.length);
                                log.a(len);
                            }
                            i2++;
                            //fos.write(bArr);

                        }
                    }catch(Exception e){
                        log.a(e);
                    }*/
                }
            });
        this.mManager = (UsbManager) getSystemService("usb");
        
		handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull android.os.Message msg) {
				//Toast.makeText(MainActivity.this,"switch",1).show();
                switch (msg.what) {
                    case DEVICE_FOUND:
						//Toast.makeText(MainActivity.this,"faund",1).show();
                        closeWaiting();
                        tvStatus.setText(getString(R.string.adb_device_connected));
                        usb_icon.setColorFilter(Color.parseColor("#4CAF50"));
                        //checkContainer.setVisibility(View.GONE);
                        //terminalView.setVisibility(View.VISIBLE);
                        //initCommand();
                        mapull.setVisibility(View.VISIBLE);
                        mapush.setVisibility(View.VISIBLE);
                        mainstall.setVisibility(View.VISIBLE);
                        maruncmd.setVisibility(View.VISIBLE);
						/*new Handler().postDelayed(new Runnable(){

								@Override
								public void run() {
									try {
										new OtgUtils.Pushnew(mcon,adbConnection, new File("/storage/emulated/0/a.apk"), "/storage/emulated/0/tmp/a.apk").execute(handler);
									} catch (Exception e) {
										log.a(e);
										//Toast.makeText(MainActivity.this,"e"+e,1).show();
									} 
									/*catch (InterruptedException e) {
										log.a(e);
										//Toast.makeText(MainActivity.this,"e2"+e,1).show();
									}*/
					/*			}
							}, 1000);
                            */
						/*try {
						//UsbManager myUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
						UsbAccessory myUsbAccessory = (mManager.getAccessoryList())[0];
						ParcelFileDescriptor pfd = mManager.openAccessory(myUsbAccessory);
						//pfd=new ParcelFileDescriptor((ParcelFileDescriptor)connection.getFileDescriptor());
						
							
							pfd = pfd.open(new File("/storage/emulated/0/aaa.apk"), pfd.MODE_READ_WRITE);
							
						
						FileDescriptor fileDescriptor = pfd.getFileDescriptor();
						//FileInputStream myFileInputStream = new FileInputStream(fileDescriptor);
						File f=new File("/storage/emulated/0/a.apk");
						int var3;
						
							FileInputStream var4 = new FileInputStream(f);
						
							FileOutputStream var6 = new FileOutputStream(fileDescriptor);
							byte[] var5 = new byte[1024];

							while(true) {
								var3 = var4.read(var5);
								if(var3 <= 0) {
									var4.close();
									var6.close();
									break;
								}
								var6.write(var5, 0, var3);
								
							}
						} catch (Exception e) {
							Toast.makeText(mcon,""+e,1).show();
						}*/
						/*
						UsbEndpoint u=mintf.getEndpoint(0);
						//u.writeToParcel();
						u.describeContents();
						try{
						//ParcelFileDescriptor p=new ParcelFileDescriptor().open();
						File f=new File("/storage/emulated/0/a.apk");
						int var3;

						FileInputStream var4 = new FileInputStream(f);

						//
					//FileOutputStream var6 = new FileOutputStream(fileDescriptor);
						byte[] var5 = new byte[1024];

						while(true) {
							var3 = var4.read(var5);
							if(var3 <= 0) {
								var4.close();
								//var6.close();
								break;
							}
							//var6.write(var5, 0, var3);
							connection.bulkTransfer(u,var5,0,var3,0);
						}
				} catch (Exception e) {
					Toast.makeText(mcon,""+e,1).show();
				}*/
						
						
						//customcommand("dpm set-device-owner com.kdroid.filter/.listener.AdminListener");
                        //showKeyboard();
                        break;

                    case CONNECTING:
                        //waitingDialog();
                        //closeKeyboard();
                        tvStatus.setText(getString(R.string.waiting_device));
                        usb_icon.setColorFilter(Color.BLUE);
                        checkContainer.setVisibility(View.VISIBLE);
                        mapull.setVisibility(View.GONE);
                        mapush.setVisibility(View.GONE);
                        mainstall.setVisibility(View.GONE);
                        maruncmd.setVisibility(View.GONE);
                        //terminalView.setVisibility(View.GONE);
                        break;

                    case DEVICE_NOT_FOUND:
                        //closeWaiting();
                        //closeKeyboard();
                        tvStatus.setText(getString(R.string.adb_device_not_connected));
                        usb_icon.setColorFilter(Color.RED);
                        mapull.setVisibility(View.GONE);
                        mapush.setVisibility(View.GONE);
                        mainstall.setVisibility(View.GONE);
                        maruncmd.setVisibility(View.GONE);
                        //checkContainer.setVisibility(View.VISIBLE);
                        //terminalView.setVisibility(View.GONE);
                        break;

                    case FLASHING:
                        Toast.makeText(MainActivity.this, "Flashing", Toast.LENGTH_SHORT).show();
                        break;

                    case INSTALLING_PROGRESS:
                        Toast.makeText(MainActivity.this, "Progress"+msg.arg1+" "+msg.arg2, Toast.LENGTH_SHORT).show();
                        break;
					
                }
            }
        };
		Toast.makeText(MainActivity.this,"fad",1).show();
        AdbBase64 base64 = new MyAdbBase64();
        try {
            adbCrypto = AdbCrypto.loadAdbKeyPair(base64, new File(getFilesDir(), "private_key"), new File(getFilesDir(), "public_key"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (adbCrypto == null) {
            try {
                adbCrypto = AdbCrypto.generateAdbKeyPair(base64);
                adbCrypto.saveAdbKeyPair(new File(getFilesDir(), "private_key"), new File(getFilesDir(), "public_key"));
            } catch (Exception e) {
                Log.w(Const.TAG, "fail to generate and save key-pair", e);
            }
        }
		Context c=this;
		IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(Message.USB_PERMISSION);
		filter.addAction(Message.USB_PERMISSIONsec);
		
		c.registerReceiver(mUsbReceiver,filter,0);
      /*  IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(Message.USB_PERMISSION);

        ContextCompat.registerReceiver(this, mUsbReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);
*/
        //Check USB
       UsbDevice device = getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);
        if (device!=null) {
            System.out.println("From Intent!");
            asyncRefreshAdbConnection(device);
        }else {
            System.out.println("From onCreate!");
            for (String k : mManager.getDeviceList().keySet()) {
                UsbDevice usbDevice = mManager.getDeviceList().get(k);
                handler.sendEmptyMessage(CONNECTING);
                if (mManager.hasPermission(usbDevice)) { 
                    asyncRefreshAdbConnection(usbDevice);
                } else {
                    mManager.requestPermission(
						usbDevice,
						PendingIntent.getBroadcast(getApplicationContext(),
												   0,
												   new Intent(Message.USB_PERMISSIONsec),
												   0));
                }
            }
        }

        //Slider
       /* sliderView = findViewById(R.id.imageSlider);
        adapter = new SliderAdapterExample(this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        SliderItem sliderItem = new SliderItem();
        sliderItem.setImageUrl(R.drawable.p2p_howto);
        sliderItem.setDescription("Connect phone to phone");
        adapter.addItem(sliderItem);

        SliderItem sliderItem1 = new SliderItem();
        sliderItem1.setImageUrl(R.drawable.deb);
        sliderItem1.setDescription("Enable developer options and USB debugging");
        adapter.addItem(sliderItem1);
*/
       //edCommand.setImeActionLabel("Run", EditorInfo.IME_ACTION_DONE);
        //edCommand.setOnEditorActionListener(this);
        //edCommand.setOnKeyListener(this);

//        //Guide
//        LinearLayout guideContainer = findViewById(R.id.guideContainer);
//        String [] split = getString(R.string.guide_for_dev_option).split("=============================");
//        for (String a:split){
//            String[] b = a.trim().split("\n");
//            String mTitle = b[0];
//            String content = a.replace(mTitle,"").replace(b[1],"").trim();
//
//            View view = getLayoutInflater().inflate(R.layout.list_item,null);
//            TextView title = view.findViewById(R.id.title);
//            title.setText(mTitle);
//            ExpandableTextView exp = view.findViewById(R.id.expand_text_view);
//            exp.setText(content);
//
//            guideContainer.addView(view);
//        }
        
    }
	public void mpermission(String mpermiss) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(mpermiss) != 0) {
                requestPermissions(new String[]{mpermiss}, 55);
            }
        }
    }
	private void closeWaiting(){
       /* if (waitingDialog!=null)
            waitingDialog.dismiss();*/
    }

    private void waitingDialog(){
        closeWaiting();
       /* waitingDialog = SpinnerDialog.displayDialog(this, "IMPORTANT ⚡",
													"You may need to accept a prompt on the target device if you are connecting "+
													"to it for the first time from this device.", false);
										*/			
    }
	public void customcommand(String cmd){
		try {
            stream = adbConnection.open("shell:");
			stream.write((cmd+"\n").getBytes("UTF-8"));
			//final String[] output = {new String(stream.read(), "US-ASCII")};
			//Toast.makeText(MainActivity.this, ""+output[0], Toast.LENGTH_SHORT).show();
			tvStatus.setText("");
			new Thread(new Runnable() {
					@Override
					public void run() {
						while (!stream.isClosed()) {
							try {
								// Print each thing we read from the shell stream
								final String[] output = {new String(stream.read(), "US-ASCII")};
								runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if (user == null) {
												user = output[0].substring(0,output[0].lastIndexOf("/")+1);
											}else if (output[0].contains(user)){
												System.out.println("End => "+user);
											}
											tvStatus.append(output[0]);
											//logs.append(output[0]);

											/*scrollView.post(new Runnable() {
													@Override
													public void run() {
														//scrollView.fullScroll(ScrollView.FOCUS_DOWN);
														//edCommand.requestFocus();
													}
												});*/
										}
									});
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
								return;
							} catch (InterruptedException e) {
								e.printStackTrace();
								return;
							} catch (IOException e) {
								e.printStackTrace();
								return;
							}
						}
					}
				}).start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
	}
	private void initCommand(){
        // Open the shell stream of ADB
        //logs.setText("");
        try {
            stream = adbConnection.open("shell:");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        // Start the receiving thread
        new Thread(new Runnable() {
				@Override
				public void run() {
					while (!stream.isClosed()) {
						try {
							// Print each thing we read from the shell stream
							final String[] output = {new String(stream.read(), "US-ASCII")};
							runOnUiThread(new Runnable() {
									@Override
									public void run() {
										if (user == null) {
											user = output[0].substring(0,output[0].lastIndexOf("/")+1);
										}else if (output[0].contains(user)){
											System.out.println("End => "+user);
										}

										//logs.append(output[0]);

										/*scrollView.post(new Runnable() {
												@Override
												public void run() {
													scrollView.fullScroll(ScrollView.FOCUS_DOWN);
													edCommand.requestFocus();
												}
											});*/
									}
								});
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							return;
						} catch (InterruptedException e) {
							e.printStackTrace();
							return;
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
					}
				}
			}).start();

        /*btnRun.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					putCommand();
				}
			});*/
    }
/*
    private void putCommand() {
        if (!edCommand.getText().toString().isEmpty()){
            // We become the sending thread
            try {
                String cmd = edCommand.getText().toString();
                if (cmd.equalsIgnoreCase("clear")) {
                    String log = logs.getText().toString();
                    String[] logSplit = log.split("\n");
                    logs.setText(logSplit[logSplit.length-1]);
                }else if (cmd.equalsIgnoreCase("exit")) {
                    finish();
                }else {
                    stream.write((cmd+"\n").getBytes("UTF-8"));
                }
                edCommand.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else Toast.makeText(MainActivity.this, "No command", Toast.LENGTH_SHORT).show();
    }
*/
    public void open(View view) {

    }
/*
    public void showKeyboard(){
        edCommand.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
*/
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					doubleBackToExitPressedOnce=false;
				}
			}, 2000);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        /* We always return false because we want to dismiss the keyboard */
       if (adbConnection != null && actionId == EditorInfo.IME_ACTION_DONE) {
           // putCommand();
        }

        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            /* Just call the onEditorAction function to handle this for us */
            return onEditorAction((TextView)v, EditorInfo.IME_ACTION_DONE, event);
        } else {
            return false;
        }
    }
	
	BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(Const.TAG, "mUsbReceiver onReceive => "+action);
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                String deviceName = device.getDeviceName();
                if (mDevice != null && mDevice.getDeviceName().equals(deviceName)) {
                    try {
                        Log.d(Const.TAG, "setAdbInterface(null, null)");
                        setAdbInterface(null, null);
                    } catch (Exception e) {
                        Log.w(Const.TAG, "setAdbInterface(null,null) failed", e);
                    }
                }
            }else if (Message.USB_PERMISSION.equals(action)){
				
				
					
				
				//Toast.makeText(MainActivity.this,"good",1).show();
				/*if(intent.getStringExtra("second")!=null){
					if(intent.getStringExtra("second").equals("second")){
					Toast.makeText(MainActivity.this,"second",1).show();
				}
			}else{*/
                System.out.println("From receiver!");
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				
					
                handler.sendEmptyMessage(Message. CONNECTING);
				
                if (mManager.hasPermission(usbDevice))
                    asyncRefreshAdbConnection(usbDevice);
                else
                    mManager.requestPermission(usbDevice,PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(Message.USB_PERMISSIONsec).putExtra("second","second"), 0));
					//}
            }
        }
    };
	public void asyncRefreshAdbConnection(final UsbDevice device) {
        if (device != null) {
            new Thread() {
                @Override
                public void run() {
                    final UsbInterface intf = findAdbInterface(device);
                    try {
                        setAdbInterface(device, intf);
                    } catch (Exception e) {
                        Log.w(Const.TAG, "setAdbInterface(device, intf) fail", e);
                    }
                }
            }.start();
        }
    }
	private UsbInterface findAdbInterface(UsbDevice device) {
        int count = device.getInterfaceCount();
        for (int i = 0; i < count; i++) {
            UsbInterface intf = device.getInterface(i);
            if (intf.getInterfaceClass() == 255 && intf.getInterfaceSubclass() == 66 &&
				intf.getInterfaceProtocol() == 1) {
                return intf;
            }
        }
        return null;
    }
	UsbDeviceConnection connection;
	UsbInterface mintf;
	private synchronized boolean setAdbInterface(UsbDevice device, UsbInterface intf) throws IOException, InterruptedException {
        if (adbConnection != null) {
            adbConnection.close();
            adbConnection = null;
            mDevice = null;
        }

        if (device != null && intf != null) {
            connection = mManager.openDevice(device);
            if (connection != null) {
                if (connection.claimInterface(intf, false)) {
					mintf=intf;
                    handler.sendEmptyMessage(CONNECTING);
                    adbConnection = AdbConnection.create(new UsbChannel(connection, intf), adbCrypto);
                    adbConnection.connect();
                    //TODO: DO NOT DELETE IT, I CAN'T EXPLAIN WHY
                    adbConnection.open("shell:exec date");

                    mDevice = device;
                    handler.sendEmptyMessage(DEVICE_FOUND);
                    return true;
                } else {
                    connection.close();
                }
            }
        }

        handler.sendEmptyMessage(DEVICE_NOT_FOUND);

        mDevice = null;
        return false;
    }
} 
