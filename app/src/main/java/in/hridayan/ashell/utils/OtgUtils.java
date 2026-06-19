package in.hridayan.ashell.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.cgutman.adblib.AdbBase64;
import com.cgutman.adblib.AdbConnection;
import com.cgutman.adblib.AdbStream;
import in.hridayan.ashell.Message;
import in.hridayan.ashell.config.Const;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import com.adbusba.log;
import java.io.DataInput;
import java.io.DataOutput;
import java.nio.charset.Charset;
import java.io.FileOutputStream;
import com.adbusba.LogUtil;
import com.adbusba.MainActivity;
import android.os.Looper;


public class OtgUtils {

  public static class MessageOtg {
    public static final int DEVICE_NOT_FOUND = 0;
    public static final int CONNECTING = 1;
    public static final int DEVICE_FOUND = 2;
    public static final int FLASHING = 3;
    public static final int INSTALLING_PROGRESS = 4;
    public static final int PUSH_PART = 5;
    public static final int PM_INST_PART = 6;
    public static final String USB_PERMISSION = "hridayan.usb.permission";
  }

  public static class ByteUtilsold {

    public static byte[] concat(byte[]... arrays) {
      // Determine the length of the result array
      int totalLength = 0;
      for (int i = 0; i < arrays.length; i++) {
        totalLength += arrays[i].length;
      }

      // create the result array
      byte[] result = new byte[totalLength];

      // copy the source arrays into the result array
      int currentIndex = 0;
      for (int i = 0; i < arrays.length; i++) {
		  currentIndex += 1;
        System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
        currentIndex += arrays[i].length;
      }

      return result;
    }

    public static final byte[] intToByteArray(int value) {
      return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
    }
  }
	public static class ByteUtils {

		public static byte[] concat(byte[]... arrays) {
			// Determine the length of the result array
			int totalLength = 0;
			for (int i = 0; i < arrays.length; i++) {
				totalLength += arrays[i].length;
			}

			// create the result array
			byte[] result = new byte[totalLength];

			// copy the source arrays into the result array
			int currentIndex = 0;
			for (int i = 0; i < arrays.length; i++) {
				//currentIndex += 1;
				System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
				currentIndex += arrays[i].length;
			}

			return result;
		}

		public static final byte[] intToByteArray(int value) {
			return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
		}
	}
  public static class UsbReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

      if (device == null) return;

      String manufacturer = device.getManufacturerName();
      String product = device.getProductName();
		if (intent!=null && (action = intent.getAction()) !=null && action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)){
			Intent intent1 = new Intent(Message.USB_PERMISSION);
			intent1.putExtra(UsbManager.EXTRA_DEVICE, (Parcelable) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE));
			context.sendBroadcast(intent1);
		}
      if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
        //showToast(context, "USB Device Attached: " + manufacturer + " " + product);
      } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
        //showToast(context, "USB Device Detached: " + manufacturer + " " + product);
        //sendIntentUponDetached(context);
		  
      }
    }
    private void showToast(Context context, String message) {
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
/*
    private void sendIntentUponDetached(Context context) {
      Intent intent = new Intent(context, MainActivity.class);
      intent.setAction("in.hridayan.ashell.ACTION_USB_DETACHED");
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }*/
  }

/*  public static class Push {

    private AdbConnection adbConnection;
    private File local;
    private String remotePath;
	Context mcon;
	AdbStream stream;
	
    public Push(Context mcon,AdbConnection adbConnection, File local, String remotePath) {
      this.adbConnection = adbConnection;
      this.local = local;
      this.remotePath = remotePath;
	  this.mcon=mcon;
    }

    public void executeold(Handler handler) throws InterruptedException, IOException {
		//Toast.makeText(mcon,"rereeeerfe",1).show();
      stream = adbConnection.open("sync:");

      String sendId = "SEND";

      String mode = ",0664";

      int length = (remotePath + mode).length();
		
      stream.write(ByteUtils.concat(sendId.getBytes(), ByteUtils.intToByteArray(length)));

		stream.write(remotePath+mode.getBytes());

     // stream.write(mode.getBytes());

      //byte[] buff = new byte[adbConnection.getMaxData()];
	  //byte[] buff = new byte[1024];
	  byte[] buff = new byte[6144];
      FileInputStream is = new FileInputStream(local);
		//mcon.append("0");
      long sent = 0;
      long total = local.length();
      int lastProgress = 0;
      while (true) {
        int read = is.read(buff);
        if (read <= 0) {
			//mcon.append("1");
          break;
        }

		  /*try {
			  ByteBuffer var105 = ByteBuffer.allocate(read + 8);
			  var105.order(ByteOrder.LITTLE_ENDIAN);
			  var105.putInt(mkid('D', 'A', 'T', 'A'));
			  var105.putInt(read);
			  var105.put(Arrays.copyOf(buff, read));
			  var105.flip();
			  stream.write(var105.array());
		  } catch (Throwable var95) {
			  //var10000 = var95;
			//  var10001 = false;
			  break;
		  }*/
		  //stream.write( ByteUtils.intToByteArray(read+8));
		 // mcon.append("2");
/*        stream.write(ByteUtils.concat("DATA".getBytes(), ByteUtils.intToByteArray(read)));

        if (read == buff.length) {
          stream.write(buff);
        } else {
          byte[] tmp = new byte[read];
          System.arraycopy(buff, 0, tmp, 0, read);
          stream.write(tmp);
			//mcon.append("3");
        }
		//  mcon.append("4");
        sent += read;

        final int progress = (int) (sent * 100 / total);
        if (lastProgress != progress) {
          handler.sendMessage(
              handler.obtainMessage(
                  Message.INSTALLING_PROGRESS, Message.PUSH_PART, progress));
          lastProgress = progress;
        }
      }

      stream.write(
          ByteUtils.concat(
              "DONE".getBytes(), ByteUtils.intToByteArray((int) System.currentTimeMillis())));
	//	mcon.append("5");
      byte[] res = stream.read();
      // TODO: test if res contains "OKEY" or "FAIL"
      Log.d(Const.TAG, new String(res));

      stream.write(ByteUtils.concat("QUIT".getBytes(), ByteUtils.intToByteArray(0)));
	//	mcon.append("6");
    }

	
		public void execute(Handler handler) throws InterruptedException, IOException {
			//Toast.makeText(mcon,"rereeeerfe",1).show();
			AdbStream stream = adbConnection.open("sync:");

			String sendId = "SEND";

			String mode = ",33152";

			int length = (remotePath + mode).length();

			stream.write(ByteUtils.concat(sendId.getBytes(), ByteUtils.intToByteArray(length)));

			stream.write(remotePath.getBytes());

			stream.write(mode.getBytes());

			//byte[] buff = new byte[adbConnection.getMaxData()];
			//byte[] buff = new byte[1024];
			byte[] buff = new byte[6144];
			FileInputStream is = new FileInputStream(local);
			//mcon.append("0");
			long sent = 0;
			long total = local.length();
			int lastProgress = 0;
			while (true) {
				int read = is.read(buff);
				if (read <= 0) {
					//mcon.append("1");
					break;
				}

				try {
				 ByteBuffer var105 = ByteBuffer.allocate(read + 8);
				 var105.order(ByteOrder.LITTLE_ENDIAN);
				 var105.putInt(mkid('D', 'A', 'T', 'A'));
				 var105.putInt(read);
				 var105.put(Arrays.copyOf(buff, read));
				 var105.flip();
				 stream.write(var105.array());
				 } catch (Throwable var95) {
				 //var10000 = var95;
				 //  var10001 = false;
				 break;
				 }
				//stream.write( ByteUtils.intToByteArray(read+8));
				// mcon.append("2");
				stream.write(ByteUtils.concat("DATA".getBytes(), ByteUtils.intToByteArray(read)));

				if (read == buff.length) {
					stream.write(buff);
				} else {
					byte[] tmp = new byte[read];
					System.arraycopy(buff, 0, tmp, 0, read);
					stream.write(tmp);
					//mcon.append("3");
				}
				//  mcon.append("4");
				sent += read;

				final int progress = (int) (sent * 100 / total);
				if (lastProgress != progress) {
					handler.sendMessage(
						handler.obtainMessage(
							Message.INSTALLING_PROGRESS, Message.PUSH_PART, progress));
					lastProgress = progress;
				}
			}

			stream.write(
				ByteUtils.concat(
					"DONE".getBytes(), ByteUtils.intToByteArray((int) System.currentTimeMillis())));
			//	mcon.append("5");
			byte[] res = stream.read();
			// TODO: test if res contains "OKEY" or "FAIL"
			Log.d(Const.TAG, new String(res));

			stream.write(ByteUtils.concat("QUIT".getBytes(), ByteUtils.intToByteArray(0)));
			//	mcon.append("6");
		}
		private final int mkid(char var1, char var2, char var3, char var4) {
			return var1 | var2 << 8 | var3 << 16 | var4 << 24;
		}
  }
  */
    public static class Pushnewcheck {

        private AdbConnection adbConnection;
        private File local;
        private String remotePath;
        Context mcon;
        AdbStream stream;

        public Pushnewcheck(Context mcon,AdbConnection adbConnection, File local, String remotePath) {
            this.adbConnection = adbConnection;
            this.local = local;
            this.remotePath = remotePath;
            this.mcon=mcon;
        }
        
        public void execute(Handler handler)  {
            //Toast.makeText(mcon,"rereeeerfe",1).show();
            try{
                stream = adbConnection.open("sync:");
                
            }catch(Exception e){
                log.a(e);
            }
            try {
                
                String s=remotePath;
                InputStream instance=new FileInputStream(local);
                OutputStream outstream=new FileOutputStream("/storage/emulated/0/bu.outstream");
                final int mkid = this.mkid('S', 'E', 'N', 'D');
                final StringBuilder sb = new StringBuilder().append(s).append(",0644");
                
                this.sendRequest(stream,mkid,sb.toString());
                final byte[] array = new byte[adbConnection.getMaxData()-9];
                log.a(adbConnection.getMaxData()-9+"isthemax");
                final int mkid2 = this.mkid('D', 'A', 'T', 'A');
                int i=0;
                while (true) {
                    log.a("loop="+i);
                    i++;
                    int read = instance.read(array);
                    if (read == -1) {
                        break;
                    }
                    final ByteBuffer allocate = ByteBuffer.allocate(read + 8);
                    allocate.order(ByteOrder.LITTLE_ENDIAN);
                    allocate.putInt(mkid2);
                    allocate.putInt(read);
                    final byte[] copy = Arrays.copyOf(array, read);
                    
                    allocate.put(copy);
                    //allocate.flip();
                    stream.write(allocate.array());
                    outstream.write(allocate.array());
                    log.a("sended"+i);
                    
                }
                log.a("done");
                final int mkid3 = this.mkid('D', 'O', 'N', 'E');
                final ByteBuffer allocate2 = ByteBuffer.allocate(8);
                log.a(0);
                allocate2.order(ByteOrder.LITTLE_ENDIAN);
                allocate2.putInt(mkid3);
                allocate2.putInt((int)(System.currentTimeMillis() / 1000));
                allocate2.flip();
                log.a(1);
                
                stream.write(allocate2. array());
                log.a(2);
                final int mkid4 = this.mkid('O', 'K', 'A', 'Y');
                final ByteBuffer allocate3 = ByteBuffer.allocate(8);
                log.a(6);
                allocate3.order(ByteOrder.LITTLE_ENDIAN);
                allocate3.putInt(mkid4);
                allocate3.putInt((int)(System.currentTimeMillis() / 1000));
                allocate3.flip();

                try {
                    final InputStream inputStream = instance;
                    log.a(3);
                    
                    
                }catch(Exception e){
                    log.a(e);
                }
                finally {
                    try {}
                    finally {
                        //instance.close();
                        log.a("instance cloce");
                        
                    }
                }
            }catch(Exception e){
                log.a(e);
            }
            finally {
                try {}
                finally {
                    try{
                        //stream.close();
                    }catch(Exception e){
                        log.a(e);
                    }
                    log.a("cloce stream");
                    
                }
            }


        }
        private final int mkid(char var1, char var2, char var3, char var4) {
            return var1 | var2 << 8 | var3 << 16 | var4 << 24;
        }
        private final void sendRequest(AdbStream writableByteChannel, int i, String str) throws IOException, InterruptedException {
            byte[] bytes = str.getBytes(Charset.forName( "UTF-8"));
            //Intrinsics.checkNotNullExpressionValue(bytes, "this as java.lang.String).getBytes(charset)");
            ByteBuffer allocate = ByteBuffer.allocate(bytes.length + 8);
            allocate.order(ByteOrder.LITTLE_ENDIAN);
            allocate.putInt(i);
            allocate.putInt(bytes.length);
            allocate.put(bytes);
            allocate.flip();
            writableByteChannel.write(allocate.array());
        }
        private final boolean readStatus(InputStream inputStream) throws IOException {
            byte[] bArr = new byte[4];
            //UtilKt.readExactly(inputStream, bArr, 4);
            try {
                stream = adbConnection.open("sync:");
                byte[] b=stream.read();
                bArr=Arrays.copyOf(b, 4);
                //inputStream.read(bArr, 0, 4);
                //String decodeToString = StringsKt.decodeToString(bArr);
                String decodeToString=new String(bArr,Charset.forName("UTF-8"));
                log.a(decodeToString);
                if (decodeToString.equals( "OKAY")) {
                    return true;
                }
                if (decodeToString.equals( "FAIL")) {
                    return false;
                }
            } catch (Exception e) {
                log.a(e);
            }

            
            return false;
        }
	}
	public static class Pushnew {

		private AdbConnection adbConnection;
		private File local;
		private String remotePath;
		MainActivity mcon;
		AdbStream stream;
        boolean installing;

		public Pushnew(MainActivity mcon,AdbConnection adbConnection, File local, String remotePath,boolean installing) {
			this.adbConnection = adbConnection;
			this.local = local;
			this.remotePath = remotePath;
			this.mcon=mcon;
            this.installing=installing;
		}

		/*public void executeold(Handler handler) throws InterruptedException, IOException {
			//Toast.makeText(mcon,"rereeeerfe",1).show();
			stream = adbConnection.open("sync:");

			String sendId = "SEND";

			String mode = ",0664";

			int length = (remotePath + mode).length();

			stream.write(ByteUtils.concat(sendId.getBytes(), ByteUtils.intToByteArray(length)));

			stream.write(remotePath+mode.getBytes());

			// stream.write(mode.getBytes());

			//byte[] buff = new byte[adbConnection.getMaxData()];
			//byte[] buff = new byte[1024];
			byte[] buff = new byte[6144];
			FileInputStream is = new FileInputStream(local);
			//mcon.append("0");
			long sent = 0;
			long total = local.length();
			int lastProgress = 0;
			while (true) {
				int read = is.read(buff);
				if (read <= 0) {
					//mcon.append("1");
					break;
				}

				/*try {
				 ByteBuffer var105 = ByteBuffer.allocate(read + 8);
				 var105.order(ByteOrder.LITTLE_ENDIAN);
				 var105.putInt(mkid('D', 'A', 'T', 'A'));
				 var105.putInt(read);
				 var105.put(Arrays.copyOf(buff, read));
				 var105.flip();
				 stream.write(var105.array());
				 } catch (Throwable var95) {
				 //var10000 = var95;
				 //  var10001 = false;
				 break;
				 }*/
				//stream.write( ByteUtils.intToByteArray(read+8));
				// mcon.append("2");
	/*			stream.write(ByteUtils.concat("DATA".getBytes(), ByteUtils.intToByteArray(read)));

				if (read == buff.length) {
					stream.write(buff);
				} else {
					byte[] tmp = new byte[read];
					System.arraycopy(buff, 0, tmp, 0, read);
					stream.write(tmp);
					//mcon.append("3");
				}
				//  mcon.append("4");
				sent += read;

				final int progress = (int) (sent * 100 / total);
				if (lastProgress != progress) {
					handler.sendMessage(
						handler.obtainMessage(
							Message.INSTALLING_PROGRESS, Message.PUSH_PART, progress));
					lastProgress = progress;
				}
			}

			stream.write(
				ByteUtils.concat(
					"DONE".getBytes(), ByteUtils.intToByteArray((int) System.currentTimeMillis())));
			//	mcon.append("5");
			byte[] res = stream.read();
			// TODO: test if res contains "OKEY" or "FAIL"
			Log.d(Const.TAG, new String(res));

			stream.write(ByteUtils.concat("QUIT".getBytes(), ByteUtils.intToByteArray(0)));
			//	mcon.append("6");
		}*/


		public void execute(Handler handler)  {
			//Toast.makeText(mcon,"rereeeerfe",1).show();
			try{
			 stream = adbConnection.open("sync:");
		/*		FileInputStream is = new FileInputStream(local);
			String sendId = "SEND";

			String mode = ",0644";

			int length = (remotePath + mode).length();

			stream.write(ByteUtils.concat(sendId.getBytes(), ByteUtils.intToByteArray(length)));

			stream.write(remotePath.getBytes());

			stream.write(mode.getBytes());

			//byte[] buff = new byte[adbConnection.getMaxData()];
			//byte[] buff = new byte[1024];
			byte[] buff = new byte[1024 * 64];
			
			//mcon.append("0");
			long sent = 0;
			long total = local.length();
			int lastProgress = 0;
			int i=0;
			while (true) {
				i++;
				log.a(stream.isClosed());
				log.a(i);
				int read = is.read(buff);
				if (read <= 0) {
					log.a("break");
					stream.write(
						ByteUtils.concat(
							"DONE".getBytes(), ByteUtils.intToByteArray((int) System.currentTimeMillis())));
					//	mcon.append("5");
					log.a("done");
					byte[] res = stream.read();
					// TODO: test if res contains "OKEY" or "FAIL"
					Log.d(Const.TAG, new String(res));

					stream.write(ByteUtils.concat("QUIT".getBytes(), ByteUtils.intToByteArray(0)));
					//	mcon.append("6");
				
				
					//mcon.append("1");
					break;
					//continue;
				}

				/*try {
					ByteBuffer var105 = ByteBuffer.allocate(read + 8);
					var105.order(ByteOrder.LITTLE_ENDIAN);
					var105.putInt(mkid('D', 'A', 'T', 'A'));
					var105.putInt(read);
					var105.put(Arrays.copyOf(buff, read));
					var105.flip();
					stream.write(var105.array());
				} catch (Throwable var95) {
					//var10000 = var95;
					//  var10001 = false;
					break;
				}*/
				//stream.write("DATA");
				//stream.write(Integer.reverseBytes(length));
				//stream.write(buffer, offset, length);
				//stream.write( ByteUtils.intToByteArray(read+8));
				// mcon.append("2");
	/*			log.a(stream.isClosed()+"2");
				log.a(read);
				DataOutput d;
				log.a(ByteUtils.concat("DATA".getBytes(), ByteUtils.intToByteArray(read)));
				stream.write("DATA".getBytes());
				stream.write(ByteUtils.intToByteArray(Integer.reverseBytes(read)));
				
				
				//stream.write(ByteUtils.concat("DATA".getBytes(), ByteUtils.intToByteArray(read)));
				log.a(stream.isClosed()+"3");
				if (read == buff.length) {
					stream.write(buff);
				} else {
					byte[] tmp = new byte[read];
					System.arraycopy(buff, 0, tmp, 0, read);
					stream.write(tmp);
					//mcon.append("3");
				}
				stream.write((",0,"+read).getBytes());
				//  mcon.append("4");
				sent += read;
				log.a(sent);
				log.a(read);
				log.a(stream.isClosed()+"l");
				/*final int progress = (int) (sent * 100 / total);
				if (lastProgress != progress) {
					handler.sendMessage(
						handler.obtainMessage(
							Message.INSTALLING_PROGRESS, Message.PUSH_PART, progress));
					lastProgress = progress;
				}*/
		//	}
/*
			stream.write(
				ByteUtils.concat(
					"DONE".getBytes(), ByteUtils.intToByteArray((int) System.currentTimeMillis())));
			//	mcon.append("5");
			log.a("done");
			byte[] res = stream.read();
			// TODO: test if res contains "OKEY" or "FAIL"
			Log.d(Const.TAG, new String(res));

			stream.write(ByteUtils.concat("QUIT".getBytes(), ByteUtils.intToByteArray(0)));
			//	mcon.append("6");
			*/
		
			
			}catch(Exception e){
				log.a(e);
			}
			try {
				//final WritableByteChannel channel = Channels.newChannel((OutputStream)androidDevice);
				String s=remotePath;
				InputStream instance=new FileInputStream(local);
				final int mkid = this.mkid('S', 'E', 'N', 'D');
				final StringBuilder sb = new StringBuilder().append(s).append(",0644");
				//Intrinsics.checkNotNullExpressionValue((Object)channel, "outChannel");
				this.sendRequest(stream,mkid,sb.toString());
				final byte[] array = new byte[adbConnection.getMaxData()-9];
				log.a(adbConnection.getMaxData()-9+"isthemax");
				final int mkid2 = this.mkid('D', 'A', 'T', 'A');
				int i=0;
				while (true) {
					log.a("loop="+i);
					i++;
					int read = instance.read(array);
					if (read == -1) {
						break;
					}
					final ByteBuffer allocate = ByteBuffer.allocate(read + 8);
					allocate.order(ByteOrder.LITTLE_ENDIAN);
					allocate.putInt(mkid2);
					allocate.putInt(read);
					final byte[] copy = Arrays.copyOf(array, read);
					//  Intrinsics.checkNotNullExpressionValue((Object)copy, "copyOf(this, newSize)");
					allocate.put(copy);
					//allocate.flip();
					stream.write(allocate.array());
					log.a("sended"+i);
					
					/* if (function1 == null) {
					 continue;
					 }
					 function1.invoke((Object)(read / (float)n));*/
				}
				log.a("done");
				final int mkid3 = this.mkid('D', 'O', 'N', 'E');
				final ByteBuffer allocate2 = ByteBuffer.allocate(8);
				log.a(0);
				allocate2.order(ByteOrder.LITTLE_ENDIAN);
				allocate2.putInt(mkid3);
				allocate2.putInt((int)(System.currentTimeMillis() / 1000));
				allocate2.flip();
				log.a(1);
				//instance=new FileInputStream(local);
				//stream = adbConnection.open("sync:");
				stream.write(allocate2. array());
				log.a(2);
				final int mkid4 = this.mkid('O', 'K', 'A', 'Y');
				final ByteBuffer allocate3 = ByteBuffer.allocate(8);
				log.a(6);
				allocate3.order(ByteOrder.LITTLE_ENDIAN);
				allocate3.putInt(mkid4);
				allocate3.putInt((int)(System.currentTimeMillis() / 1000));
				allocate3.flip();
				//stream.write(allocate3. array());
				//instance = adbConnect.getInputStream();
                if(installing){
                    new Handler(Looper.getMainLooper()).post(new Runnable(){
                            @Override
                            public void run() {
                                mcon.customcommand("chmod 777 /data/local/tmp/tmp.apk");
                                mcon.customcommand("pm install -r /data/local/tmp/tmp.apk\nrm /data/local/tmp/tmp.apk");
                                
                            }
                        });
                                        //mcon.customcommand("rm /data/local/tmp/tmp.apk");
                }
				try {
					//final InputStream inputStream = instance;
					//log.a(3);
					// Intrinsics.checkNotNullExpressionValue((Object)inputStream, "it");
					/*if (this.readStatus(inputStream)) {
						log.a(4+""+readStatus(inputStream));
						//final Unit instance2 = Unit.INSTANCE;
						//  CloseableKt.closeFinally((Closeable)instance, (Throwable)null);
						//instance = (InputStream)Unit.INSTANCE;
						//  CloseableKt.closeFinally((Closeable)androidDevice, (Throwable)null);
						return;
					}
					final StringBuilder sb2 = new StringBuilder();
					sb2.append("Could not push to ");
					sb2.append(s);
					log.a(sb2);
					throw new RuntimeException(sb2.toString());
					*/
				}catch(Exception e){
					log.a(e);
				}
				finally {
					try {}
					finally {
						//instance.close();
						log.a("instance close");
						//  CloseableKt.closeFinally((Closeable)instance, (Throwable)s);
					}
				}
			}catch(Exception e){
				log.a(e);
			}
			finally {
				try {}
				finally {
					try{
					//stream.close();
					}catch(Exception e){
						log.a(e);
					}
					log.a("close stream");
					// CloseableKt.closeFinally((Closeable)androidDevice, (Throwable)s);
				}
			}
			
			
		}
		private final int mkid(char var1, char var2, char var3, char var4) {
			return var1 | var2 << 8 | var3 << 16 | var4 << 24;
		}
		private final void sendRequest(AdbStream writableByteChannel, int i, String str) throws IOException, InterruptedException {
			byte[] bytes = str.getBytes(Charset.forName( "UTF-8"));
			//Intrinsics.checkNotNullExpressionValue(bytes, "this as java.lang.String).getBytes(charset)");
			ByteBuffer allocate = ByteBuffer.allocate(bytes.length + 8);
			allocate.order(ByteOrder.LITTLE_ENDIAN);
			allocate.putInt(i);
			allocate.putInt(bytes.length);
			allocate.put(bytes);
			allocate.flip();
			writableByteChannel.write(allocate.array());
		}
		private final boolean readStatus(InputStream inputStream) throws IOException {
			byte[] bArr = new byte[4];
			//UtilKt.readExactly(inputStream, bArr, 4);
			try {
				stream = adbConnection.open("sync:");
				byte[] b=stream.read();
				bArr=Arrays.copyOf(b, 4);
			//inputStream.read(bArr, 0, 4);
			//String decodeToString = StringsKt.decodeToString(bArr);
			String decodeToString=new String(bArr,Charset.forName("UTF-8"));
			log.a(decodeToString);
			if (decodeToString.equals( "OKAY")) {
				return true;
			}
			if (decodeToString.equals( "FAIL")) {
				return false;
			}
			} catch (Exception e) {
				log.a(e);
			}
			
			/*String readProtocolString = readProtocolString(inputStream);
			decodeToString = TAG;
			StringBuilder stringBuilder = new StringBuilder("Failed to read status: ");
			stringBuilder.append(readProtocolString);
			Log.e(decodeToString, stringBuilder.toString());*/
			return false;
		}
	}
  public static class ExternalCmdStore {
    private static SharedPreferences sharedPreferences;
    private static String CMD_KEY = "cmd_key";

    private static void initShared(Context context) {
      if (sharedPreferences == null)
        sharedPreferences = context.getSharedPreferences("cmd", Context.MODE_PRIVATE);
    }

    public static void put(Context context, String cmd) {
      initShared(context);
      sharedPreferences.edit().putString(CMD_KEY, cmd).apply();
    }

    public static String get(Context context) {
      initShared(context);
      return sharedPreferences.getString(CMD_KEY, null);
    }
  }
	private final int mkid(char var1, char var2, char var3, char var4) {
		return var1 | var2 << 8 | var3 << 16 | var4 << 24;
	}
	/*public final void push(AndroidDevice var1, InputStream var2, String var3, long var4) {
		Socket var97 = this.adbConnect(var1, "sync:");
		Closeable var10 = (Closeable)var97.getOutputStream();

		Throwable var10000;
		boolean var10001;
		Throwable var98;
		Closeable var99;
		label630: {
			label625: {
				int var8;
				byte[] var12;
				WritableByteChannel var103;
				try {
					var103 = Channels.newChannel((OutputStream)var10);
					var8 = this.mkid('S', 'E', 'N', 'D');
					StringBuilder var11 = new StringBuilder();
					var11.append(var3);
					var11.append(",33152");
					String var104 = var11.toString();
					//Intrinsics.checkNotNullExpressionValue(var103, "outChannel");
					this.sendRequest(var103, var8, var104);
					var12 = new byte[6144];
					var8 = this.mkid('D', 'A', 'T', 'A');
				} catch (Throwable var96) {
					var10000 = var96;
					var10001 = false;
					break label625;
				}

				while(true) {
					int var9;
					try {
						var9 = var2.read(var12);
					} catch (Throwable var94) {
						var10000 = var94;
						var10001 = false;
						break;
					}

					if (var9 == -1) {
						try {
							var8 = this.mkid('D', 'O', 'N', 'E');
							ByteBuffer var100 = ByteBuffer.allocate(8);
							var100.order(ByteOrder.LITTLE_ENDIAN);
							var100.putInt(var8);
							var100.putInt((int)(System.currentTimeMillis() / (long)1000));
							var100.flip();
							var103.write(var100);
							var99 = (Closeable)var97.getInputStream();
							break label630;
						} catch (Throwable var93) {
							var10000 = var93;
							var10001 = false;
							break;
						}
					}

					try {
						ByteBuffer var105 = ByteBuffer.allocate(var9 + 8);
						var105.order(ByteOrder.LITTLE_ENDIAN);
						var105.putInt(var8);
						var105.putInt(var9);
						var105.put(Arrays.copyOf(var12, var9));
						var105.flip();
						var103.write(var105);
					} catch (Throwable var95) {
						var10000 = var95;
						var10001 = false;
						break;
					}

					
				}
			}

			var98 = var10000;

			try {
				throw var98;
			} finally {
				;
			}
		}

		label611: {
			try {
				if (this.readStatus((InputStream)var99)) {
					return;
				}
			} catch (Throwable var92) {
				var10000 = var92;
				var10001 = false;
				break label611;
			}

			label605:
			try {
				StringBuilder var101 = new StringBuilder();
				var101.append("Could not push to ");
				var101.append(var3);
				RuntimeException var102 = new RuntimeException(var101.toString());
				throw var102;
			} catch (Throwable var91) {
				var10000 = var91;
				var10001 = false;
				break label605;
			}
		}

		var98 = var10000;

		
	}
	*/
	
  public static class Install {
    private AdbConnection adbConnection;
    private String remotePath;
    private long installTimeAssumption = 0;

    public Install(AdbConnection adbConnection, String remotePath, long installTimeAssumption) {
      this.adbConnection = adbConnection;
      this.remotePath = remotePath;
      this.installTimeAssumption = installTimeAssumption;
    }

    public void execute(final Handler handler) throws IOException, InterruptedException {
      final AtomicBoolean done = new AtomicBoolean(false);
      try {
        AdbStream stream = adbConnection.open("shell:pm install -r " + remotePath);
        // we assume installation will take installTimeAssumption milliseconds.
        new Thread() {
          @Override
          public void run() {
            int percent = 0;

            while (!done.get()) {
              handler.sendMessage(
                  handler.obtainMessage(
                      MessageOtg.INSTALLING_PROGRESS, MessageOtg.PM_INST_PART, percent));

              if (percent < 95) {
                percent += 1;
                try {
                  Thread.sleep(installTimeAssumption / 100);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            }
          }
        }.start();

        while (!stream.isClosed()) {
          try {
            Log.d(Const.TAG, new String(stream.read()));
          } catch (IOException e) {
            // there must be a Stream Close Exception
            break;
          }
        }
      } finally {
        done.set(true);
        handler.sendMessage(
            handler.obtainMessage(MessageOtg.INSTALLING_PROGRESS, MessageOtg.PM_INST_PART, 100));
      }
    }
  }

  public static class MyAdbBase64 implements AdbBase64 {
    @Override
    public String encodeToString(byte[] data) {
      return Base64.encodeToString(data, Base64.NO_WRAP);
    }
  }
  /*
	public final void push(AndroidDevice var1, InputStream var2, String var3, long var4, Function1<? super Float, Unit> var6) {
		//Intrinsics.checkNotNullParameter(var2, "srcFileStream");
		//Intrinsics.checkNotNullParameter(var3, "destFilePath");
		Socket var10 = this.adbConnect(var1, "sync:");
		Closeable var146 = (Closeable)var10.getOutputStream();

		Throwable var10000;
		label931: {
			int var7;
			WritableByteChannel WritableByteChannel;
			byte[] var12;
			boolean var10001;
			try {
				WritableByteChannel = Channels.newChannel((OutputStream)var146);
				var7 = this.mkid('S', 'E', 'N', 'D');
				StringBuilder var11 = new StringBuilder();
				var11.append(var3);
				var11.append(",33152");
				String var156 = var11.toString();
				//Intrinsics.checkNotNullExpressionValue(var9, "outChannel");
				this.sendRequest(WritableByteChannel, var7, var156);
				var12 = new byte[6144];
				var7 = this.mkid('D', 'A', 'T', 'A');
			} catch (Throwable var145) {
				var10000 = var145;
				var10001 = false;
				break label931;
			}

			while(true) {
				int var8;
				try {
					var8 = var2.read(var12);
				} catch (Throwable var142) {
					var10000 = var142;
					var10001 = false;
					break;
				}

				if (var8 == -1) {
					Closeable var148;
					try {
						var7 = this.mkid('D', 'O', 'N', 'E');
						ByteBuffer var147 = ByteBuffer.allocate(8);
						var147.order(ByteOrder.LITTLE_ENDIAN);
						var147.putInt(var7);
						var147.putInt((int)(System.currentTimeMillis() / (long)1000));
						var147.flip();
						WritableByteChannel.write(var147);
						var148 = (Closeable)var10.getInputStream();
					} catch (Throwable var141) {
						var10000 = var141;
						var10001 = false;
						break;
					}

					label937: {
						label914: {
							try {
								InputStream var152 = (InputStream)var148;
								//Intrinsics.checkNotNullExpressionValue(var152, "it");
								if (this.readStatus(var152)) {
									Unit var150 = Unit.INSTANCE;
									break label914;
								}
							} catch (Throwable var140) {
								var10000 = var140;
								var10001 = false;
								break label937;
							}

							try {
								StringBuilder var154 = new StringBuilder();
								var154.append("Could not push to ");
								var154.append(var3);
								RuntimeException var155 = new RuntimeException(var154.toString());
								throw var155;
							} catch (Throwable var139) {
								var10000 = var139;
								var10001 = false;
								break label937;
							}
						}

						try {
							//CloseableKt.closeFinally(var148, (Throwable)null);
							Unit var151 = Unit.INSTANCE;
						} catch (Throwable var138) {
							var10000 = var138;
							var10001 = false;
							break;
						}

						CloseableKt.closeFinally(var146, (Throwable)null);
						return;
					}

					Throwable var153 = var10000;

					try {
						throw var153;
					} finally {
						try {
							CloseableKt.closeFinally(var148, var153);
						} catch (Throwable var136) {
							var10000 = var136;
							var10001 = false;
							break;
						}
					}
				}

				try {
					ByteBuffer var157 = ByteBuffer.allocate(var8 + 8);
					var157.order(ByteOrder.LITTLE_ENDIAN);
					var157.putInt(var7);
					var157.putInt(var8);
					byte[] var13 = Arrays.copyOf(var12, var8);
					//Intrinsics.checkNotNullExpressionValue(var13, "copyOf(this, newSize)");
					var157.put(var13);
					var157.flip();
					WritableByteChannel.write(var157);
				} catch (Throwable var144) {
					var10000 = var144;
					var10001 = false;
					break;
				}

				if (var6 != null) {
					try {
						var6.invoke((float)var8 / (float)var4);
					} catch (Throwable var143) {
						var10000 = var143;
						var10001 = false;
						break;
					}
				}
			}
		}

		Throwable var149 = var10000;

		try {
			throw var149;
		} finally {
			//CloseableKt.closeFinally(var146, var149);
		}
	}*/
	/*public void npush(InputStream instance,String s) throws IOException{
		try {
            final WritableByteChannel channel = Channels.newChannel((OutputStream)androidDevice);
            final int mkid = this.mkid('S', 'E', 'N', 'D');
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(",33152");
            final String string = sb.toString();
            //Intrinsics.checkNotNullExpressionValue((Object)channel, "outChannel");
            this.sendRequest(channel, mkid, string);
            final byte[] array = new byte[6144];
            final int mkid2 = this.mkid('D', 'A', 'T', 'A');
            while (true) {
                final int read = instance.read(array);
                if (read == -1) {
                    break;
                }
                final ByteBuffer allocate = ByteBuffer.allocate(read + 8);
                allocate.order(ByteOrder.LITTLE_ENDIAN);
                allocate.putInt(mkid2);
                allocate.putInt(read);
                final byte[] copy = Arrays.copyOf(array, read);
				//  Intrinsics.checkNotNullExpressionValue((Object)copy, "copyOf(this, newSize)");
                allocate.put(copy);
                allocate.flip();
                channel.write(allocate);
				/* if (function1 == null) {
				 continue;
				 }
				 function1.invoke((Object)(read / (float)n));*/
    /*        }
            final int mkid3 = this.mkid('D', 'O', 'N', 'E');
            final ByteBuffer allocate2 = ByteBuffer.allocate(8);
            allocate2.order(ByteOrder.LITTLE_ENDIAN);
            allocate2.putInt(mkid3);
            allocate2.putInt((int)(System.currentTimeMillis() / 1000));
            allocate2.flip();
            channel.write(allocate2);
            instance = adbConnect.getInputStream();
            try {
                final InputStream inputStream = instance;
				// Intrinsics.checkNotNullExpressionValue((Object)inputStream, "it");
                if (this.readStatus(inputStream)) {
                    //final Unit instance2 = Unit.INSTANCE;
					//  CloseableKt.closeFinally((Closeable)instance, (Throwable)null);
                    //instance = (InputStream)Unit.INSTANCE;
					//  CloseableKt.closeFinally((Closeable)androidDevice, (Throwable)null);
                    return;
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Could not push to ");
                sb2.append(s);
                throw new RuntimeException(sb2.toString());
            }
            finally {
                try {}
                finally {
					//  CloseableKt.closeFinally((Closeable)instance, (Throwable)s);
                }
            }
        }
        finally {
            try {}
            finally {
				// CloseableKt.closeFinally((Closeable)androidDevice, (Throwable)s);
            }
        }
	}
    public final void push(AndroidDevice androidDevice, InputStream instance, final String s, final long n, final Function1<? super Float, Unit> function1) throws IOException {
        //Intrinsics.checkNotNullParameter((Object)instance, "srcFileStream");
        //Intrinsics.checkNotNullParameter((Object)s, "destFilePath");
        final Socket adbConnect = this.adbConnect(androidDevice, "sync:");
        androidDevice = (AndroidDevice)adbConnect.getOutputStream();
        try {
            final WritableByteChannel channel = Channels.newChannel((OutputStream)androidDevice);
            final int mkid = this.mkid('S', 'E', 'N', 'D');
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(",33152");
            final String string = sb.toString();
            //Intrinsics.checkNotNullExpressionValue((Object)channel, "outChannel");
            this.sendRequest(channel, mkid, string);
            final byte[] array = new byte[6144];
            final int mkid2 = this.mkid('D', 'A', 'T', 'A');
            while (true) {
                final int read = instance.read(array);
                if (read == -1) {
                    break;
                }
                final ByteBuffer allocate = ByteBuffer.allocate(read + 8);
                allocate.order(ByteOrder.LITTLE_ENDIAN);
                allocate.putInt(mkid2);
                allocate.putInt(read);
                final byte[] copy = Arrays.copyOf(array, read);
              //  Intrinsics.checkNotNullExpressionValue((Object)copy, "copyOf(this, newSize)");
                allocate.put(copy);
                allocate.flip();
                channel.write(allocate);
               /* if (function1 == null) {
                    continue;
                }
                function1.invoke((Object)(read / (float)n));*/
   /*         }
            final int mkid3 = this.mkid('D', 'O', 'N', 'E');
            final ByteBuffer allocate2 = ByteBuffer.allocate(8);
            allocate2.order(ByteOrder.LITTLE_ENDIAN);
            allocate2.putInt(mkid3);
            allocate2.putInt((int)(System.currentTimeMillis() / 1000));
            allocate2.flip();
            channel.write(allocate2);
            instance = adbConnect.getInputStream();
            try {
                final InputStream inputStream = instance;
               // Intrinsics.checkNotNullExpressionValue((Object)inputStream, "it");
                if (this.readStatus(inputStream)) {
                    //final Unit instance2 = Unit.INSTANCE;
                  //  CloseableKt.closeFinally((Closeable)instance, (Throwable)null);
                    //instance = (InputStream)Unit.INSTANCE;
                  //  CloseableKt.closeFinally((Closeable)androidDevice, (Throwable)null);
                    return;
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Could not push to ");
                sb2.append(s);
                throw new RuntimeException(sb2.toString());
            }
            finally {
                try {}
                finally {
                  //  CloseableKt.closeFinally((Closeable)instance, (Throwable)s);
                }
            }
        }
        finally {
            try {}
            finally {
               // CloseableKt.closeFinally((Closeable)androidDevice, (Throwable)s);
            }
        }
    }
	private final boolean readStatus(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[4];
        //UtilKt.readExactly(inputStream, bArr, 4);
		inputStream.read(bArr,0,4);
        //String decodeToString = StringsKt.decodeToString(bArr);
		String decodeToString=new String(bArr,Charset.forName("UTF-8"));
        if (decodeToString.equals( "OKAY")) {
            return true;
        }
        if (decodeToString.equals( "FAIL")) {
            return false;
        }
        String readProtocolString = readProtocolString(inputStream);
        decodeToString = TAG;
        StringBuilder stringBuilder = new StringBuilder("Failed to read status: ");
        stringBuilder.append(readProtocolString);
        Log.e(decodeToString, stringBuilder.toString());
        return false;
    }
	private final void sendRequest(WritableByteChannel writableByteChannel, int i, String str) throws IOException {
        byte[] bytes = str.getBytes(Charset.forName( "UTF-8"));
        //Intrinsics.checkNotNullExpressionValue(bytes, "this as java.lang.String).getBytes(charset)");
        ByteBuffer allocate = ByteBuffer.allocate(bytes.length + 8);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        allocate.putInt(i);
        allocate.putInt(bytes.length);
        allocate.put(bytes);
        allocate.flip();
        writableByteChannel.write(allocate);
    }*/
    public static class PullexNew {
        private AdbConnection adbConnection;
        private File local;
        private String remotePath;
        AdbStream stream;

        public void execute(Handler handler) {
            String str = "close stream";
            try {
                this.stream = this.adbConnection.open("sync:");
                FileOutputStream fileOutputStream = new FileOutputStream(this.local);
                this.stream.write(ByteUtils.concat(new byte[][]{"RECV".getBytes(), ByteUtils.intToByteArray(this.remotePath.length())}));
                this.stream.write(this.remotePath.getBytes());
                int i = 0;
                boolean skip8new=false;
                int msizeTotal=0;
                int msizeCount=0;
                while (true) {
                    LogUtil.logToFile("start-loop="+i);
                    i++;
                    byte[] bArr = null;
                    byte[] bArr2 = null;
                    byte[] bArr4=null;
                    bArr2 = this.stream.read();
                    LogUtil.logToFile(bArr2.length+"islength");
                    bArr2 = Arrays.copyOfRange(bArr2, 0, bArr2.length);
                    if (bArr2 != null && bArr2.length == -1) {
                        break;
                    }
                    try {
                        
                        if (bArr2 != null && bArr2.length > 8) {
                            bArr = Arrays.copyOfRange(bArr2, bArr2.length - 8, bArr2.length - 4);
                            if(i==1){
                                //only first...
                                bArr4 = Arrays.copyOfRange(bArr2, 0, 4);
                            }
                            
                        }
                        String str3="";
                        if(bArr4!=null)
                        str3 = new String(bArr4, Charset.forName("UTF-8"));
                        log.a(str3);
                        if (str3.equals("DATA")) {
                            if (bArr2 != null && bArr2.length > 8) {
                                
                                final ByteBuffer allocate = ByteBuffer.wrap(bArr2, 4, 8);
                                allocate.order(ByteOrder.LITTLE_ENDIAN);
                                msizeTotal=allocate.getInt();
                                msizeCount=0;
                                log.a("int length pull="+msizeTotal);
                                skip8new=true;
                                msizeCount+=bArr2.length-8;
                            }
                        }else if((msizeCount+bArr2.length)>msizeTotal){
                            //read only siz-sizover
                            int readonl=msizeTotal-msizeCount;
                            if(readonl!=0)
                                fileOutputStream.write(bArr2, 0, readonl);
                            LogUtil.logToFile("2int length readonl="+readonl);
                            //read data and siz & skip 8 bytes 
                            bArr4 = Arrays.copyOfRange(bArr2, readonl, readonl+ 4);
                            str3="";
                            if(bArr4!=null){
                                str3 = new String(bArr4, Charset.forName("UTF-8"));
                            }else{LogUtil.logToFile("nullll...");}
                            log.a(str3);
                            byte[] bArr5 = Arrays.copyOfRange(bArr2, readonl+4, readonl+ 8);
                            final ByteBuffer allocate = ByteBuffer.wrap(bArr5);
                            allocate.order(ByteOrder.LITTLE_ENDIAN);
                            msizeTotal=allocate.getInt();
                            msizeCount=0;
                            LogUtil.logToFile("2int length pull="+msizeTotal);
                            //read bArr2.length-(readonl+8)
                            msizeCount+=bArr2.length-(readonl+8);
                            fileOutputStream.write(bArr2, readonl+8, msizeCount);
                            LogUtil.logToFile("2int length msizeCount="+msizeCount);
                            //end & continue
                           
                            LogUtil.logToFile(new StringBuffer().append("pulled").append(i).toString());
                            continue;//no rewrite...
                        }else{
                            msizeCount+=bArr2.length;
                        }
                        
                        
                        String str2 = new String(bArr, Charset.forName("UTF-8"));
                        log.a(str2);
                        if (str2.equals("DONE")) {
                            if (bArr2 != null && bArr2.length > 8) {
                                fileOutputStream.write(bArr2, 0, bArr2.length - 8);
                                break;
                            }
                        } else if (bArr2 != null) {
                            if(skip8new){
                                fileOutputStream.write(bArr2, 8, bArr2.length-8);
                                skip8new=false;
                            }else
                                fileOutputStream.write(bArr2, 0, bArr2.length);
                        }
                       
                    } catch (Exception e) {
                        LogUtil.logToFile(e,true);
                        
                    }
                    LogUtil.logToFile("pulled"+i);
                }
                handler.sendMessage(
                    handler.obtainMessage(MessageOtg.INSTALLING_PROGRESS, MessageOtg.PM_INST_PART, 100));
                LogUtil.logToFile("done");
            } catch (Exception e2) {
                LogUtil.logToFile(e2);
            } catch (Throwable th) {
                try {
                    this.stream.close();
                } catch (Exception e3) {
                    LogUtil.logToFile(e3);
                }
                LogUtil.logToFile(str);
            }
            try {
                this.stream.close();
            } catch (Exception e22) {
                LogUtil.logToFile(e22);
            }
            LogUtil.logToFile(str);
        }

        public PullexNew(AdbConnection adbConnection, File file, String str) {
            this.adbConnection = adbConnection;
            this.local = file;
            this.remotePath = str;
        }
    }
    public static class Pullex {
        private AdbConnection adbConnection;
        private File local;
        private String remotePath;
        AdbStream stream;

        public void execute(Handler handler) {
            String str = "cloce stream";
            try {
                this.stream = this.adbConnection.open("sync:");
                FileOutputStream fileOutputStream = new FileOutputStream(this.local);
                this.stream.write(ByteUtils.concat(new byte[][]{"RECV".getBytes(), ByteUtils.intToByteArray(this.remotePath.length())}));
                this.stream.write(this.remotePath.getBytes());
                int i = 0;
                int i2=1;
                int i3=1;
                boolean skip8=false;
                boolean skip8new=false;
                while (true) {
                    log.a(new StringBuffer().append("loop=").append(i).toString());
                    i++;
                    //if is 1 - skip
                    //if is 17 skip && i2 = 1
                    if(i2==1){
                        skip8=true;
                    }else if(i2==17){
                        skip8=true;
                        i2=1;
                        i3++;
                        if(i3==17)i3=1;
                    }else{
                        skip8=false;
                    }
                    
                    /*
                    if(i2!=1&&i2<18){
                        skip8=false;
                        
                    }else{
                        i2=1;
                        skip8=true;
                    }
                    i2++;
                    */
                    byte[] bArr = null;
                    byte[] bArr2 = null;
                    byte[] bArr4=null;
                    bArr2 = this.stream.read();
                    log.a(new StringBuffer().append(bArr2.length).append("islength").toString());
                    int length = bArr2.length;
                    bArr2 = Arrays.copyOfRange(bArr2, 0, bArr2.length);
                    if (bArr2 != null && bArr2.length == -1) {
                        break;
                    }
                    try {
                        byte[] bArr3 = null;
                        if (bArr2 != null && bArr2.length > 8) {
                            bArr = Arrays.copyOfRange(bArr2, bArr2.length - 8, bArr2.length - 4);
                            bArr4 = Arrays.copyOfRange(bArr2, 0, 4);
                            //bArr = Arrays.copyOfRange(bArr2, i3, i3+8);
                        }
                        String str3 = new String(bArr4, Charset.forName("UTF-8"));
                        log.a(str3);
                        if (str3.equals("DATA")) {
                            if (bArr2 != null && bArr2.length > 8) {
                                /*final ByteBuffer allocate = ByteBuffer.allocate(4);
                                allocate.order(ByteOrder.LITTLE_ENDIAN);
                                allocate.get(bArr2, 4, 8);*/
                                final ByteBuffer allocate = ByteBuffer.wrap(bArr2, 4, 8);
                                allocate.order(ByteOrder.LITTLE_ENDIAN);
                                //allocate.get(bArr2, 4, 8);
                                int siz=allocate.getInt();
                                log.a("int length pull="+siz);
                                skip8new=true;
                            }
                        }
                        String str2 = new String(bArr, Charset.forName("UTF-8"));
                        log.a(str2);
                        if (str2.equals("DONE")) {
                            if (bArr2 != null && bArr2.length > 8) {
                                fileOutputStream.write(bArr2, 0, bArr2.length - 8);
                                break;
                            }
                        } else if (bArr2 != null) {
                            if(skip8new){
                                //fileOutputStream.write(bArr2, 8*i3, bArr2.length-(8*i3));
                                //fileOutputStream.write(bArr2, bArr2.length-(8*i3)+8, (8*i3)-8);
                                fileOutputStream.write(bArr2, 8, bArr2.length-8);
                                skip8new=false;
                            }else
                            fileOutputStream.write(bArr2, 0, bArr2.length);
                        }
                        i2++;
                    } catch (Exception e) {
                        log.a(e);
                    }
                    log.a(new StringBuffer().append("pulled").append(i).toString());
                    
                }
                handler.sendMessage(
                    handler.obtainMessage(MessageOtg.INSTALLING_PROGRESS, MessageOtg.PM_INST_PART, 100));
                log.a("done");
            } catch (Exception e2) {
                log.a(e2);
            } catch (Throwable th) {
                try {
                    this.stream.close();
                } catch (Exception e3) {
                    log.a(e3);
                }
                log.a(str);
            }
            try {
                this.stream.close();
            } catch (Exception e22) {
                log.a(e22);
            }
            log.a(str);
        }

        public Pullex(AdbConnection adbConnection, File file, String str) {
            this.adbConnection = adbConnection;
            this.local = file;
            this.remotePath = str;
        }
    }
    public class Pull {
        private AdbConnection adbConnection;
        private File local;
        private String remotePath;
        AdbStream stream;

        public void execute(Handler handler) {
            int i;
            long j;
            int i2;
            Object e;
            String str = "cloce stream";
            try {
                this.stream = this.adbConnection.open("sync:");
                FileOutputStream fileOutputStream = new FileOutputStream(this.local);
                AdbStream adbStream = this.stream;
                byte[][] bArr = new byte[2][];
                bArr[0] = "RECV".getBytes();
                int i3 = 1;
                bArr[1] = ByteUtils.intToByteArray(this.remotePath.length());
                adbStream.write(ByteUtils.concat(bArr));
                this.stream.write(this.remotePath.getBytes());
                long j2 = (long) 0;
                int i4 = 65536;
                int i5 = 16;
                int i6 = 0;
                int i7 = 8;
                while (true) {
                    byte[] copyOfRange;
                    byte[] bArr2;
                    byte[] bArr3;
                    log.a(new StringBuffer().append("loop=").append(i6).toString());
                    i6++;
                    byte[] bArr4 = null;
                    byte[] bArr5 = null;
                    bArr5 = null;
                    bArr5 = null;
                    bArr5 = this.stream.read();
                    log.a(new StringBuffer().append(bArr5.length).append("islength").toString());
                    j2 += (long) bArr5.length;
                    long j3 = (long) i4;
                    if (j2 > j3) {
                        byte[] copyOfRange2;
                        i = i3;
                        if (j2 - j3 == ((long) bArr5.length)) {
                            copyOfRange = (bArr5.length < i7 || i7 <= 0) ? null : Arrays.copyOfRange(bArr5, 0, i7);
                            i7 += 8;
                            copyOfRange2 = bArr5.length > i5 ? Arrays.copyOfRange(bArr5, i5, bArr5.length) : null;
                            i5 += 8;
                            log.a(new StringBuffer().append(i7).append("is toone").toString());
                            log.a(new StringBuffer().append(i5).append("is fromtwo").toString());
                            if (i5 == 5004) {
                                i5 -= 4096;
                                i4 += 4096;
                            }
                            if (i7 >= 4096) {
                                i7 -= 4096;
                            }
                        } else {
                            copyOfRange2 = null;
                            copyOfRange = copyOfRange2;
                        }
                        log.a(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(bArr5.length).append("curlength").toString()).append(j2).toString()).append("cursent").toString());
                        j2 -= (long) i4;
                        log.a("skipdata");
                        bArr2 = copyOfRange;
                        copyOfRange = copyOfRange2;
                        j = j2;
                        i2 = i4;
                        bArr3 = null;
                    } else {
                        int i8 = i3 != 0 ? 8 : 0;
                        copyOfRange = null;
                        bArr2 = copyOfRange;
                        i = 0;
                        long j4 = j2;
                        i2 = i4;
                        bArr3 = Arrays.copyOfRange(bArr5, i8, bArr5.length);
                        j = j4;
                    }
                    if (bArr3 == null || bArr3.length != -1) {
                        if (copyOfRange != null && copyOfRange.length == -1) {
                            break;
                        }
                        try {
                            bArr5 = null;
                            if (bArr3 != null && bArr3.length > 8) {
                                bArr4 = Arrays.copyOfRange(bArr3, bArr3.length - 8, bArr3.length - 4);
                            }
                            if (bArr2 != null && bArr2.length > 8) {
                                bArr4 = Arrays.copyOfRange(bArr2, bArr2.length - 8, bArr2.length - 4);
                            }
                            if (copyOfRange != null && copyOfRange.length > 8) {
                                bArr4 = Arrays.copyOfRange(copyOfRange, copyOfRange.length - 8, copyOfRange.length - 4);
                            }
                            String str2 = new String(bArr4, Charset.forName("UTF-8"));
                            log.a(str2);
                            if (str2.equals("DONE")) {
                                if (bArr3 == null || bArr3.length <= 8) {
                                    if (bArr2 != null && bArr2.length > 8) {
                                        fileOutputStream.write(bArr2, 0, bArr2.length - 8);
                                        break;
                                    } else if (copyOfRange != null) {
                                        try {
                                            if (copyOfRange.length > 8) {
                                                try {
                                                    fileOutputStream.write(copyOfRange, 0, copyOfRange.length - 8);
                                                } catch (Exception e2) {
                                                    e = e2;
                                                }
                                            }
                                        } catch (Exception e3) {
                                            e = e3;
                                            log.a(e);
                                            log.a(new StringBuffer().append("pulled").append(i6).toString());
                                            i4 = i2;
                                            j2 = j;
                                            i3 = i;
                                        }
                                    }
                                } else {
                                    fileOutputStream.write(bArr3, 0, bArr3.length - 8);
                                    break;
                                }
                            }
                            if (bArr3 != null) {
                                fileOutputStream.write(bArr3, 0, bArr3.length);
                            }
                            if (bArr2 != null) {
                                fileOutputStream.write(bArr2, 0, bArr2.length);
                            }
                            if (copyOfRange != null) {
                                try {
                                    fileOutputStream.write(copyOfRange, 0, copyOfRange.length);
                                } catch (Exception e4) {
                                    e = e4;
                                }
                            }
                        } catch (Exception e5) {
                            e = e5;
                            log.a(e);
                            log.a(new StringBuffer().append("pulled").append(i6).toString());
                            i4 = i2;
                            j2 = j;
                            i3 = i;
                        }
                        log.a(new StringBuffer().append("pulled").append(i6).toString());
                        i4 = i2;
                        j2 = j;
                        i3 = i;
                    } else {
                        break;
                    }
                }
                log.a("done");
               // break;
            } catch (Exception e6) {
                log.a(e6);
            } catch (Throwable th) {
                log.a(str);
                //throw th;
            }
            log.a(str);
        }

        public Pull(AdbConnection adbConnection, File file, String str) {
            this.adbConnection = adbConnection;
            this.local = file;
            this.remotePath = str;
        }
    }
    public class Pullnew {
        private AdbConnection adbConnection;
        private File local;
        Context mcon;
        private String remotePath;
        AdbStream stream;

        public void execute(Handler handler) {
            int i;
            Object e;
            String str = "instance cloce";
            String str2 = "cloce stream";
            try {
                this.stream = this.adbConnection.open("sync:");
            } catch (Exception e2) {
                log.a(e2);
            }
            try {
                String str3 = this.remotePath;
                FileOutputStream fileOutputStream = new FileOutputStream(this.local);
                sendRequest(this.stream, mkid('R', 'E', 'C', 'V'), str3);
                byte[] bArr = new byte[(this.adbConnection.getMaxData() - 9)];
                log.a(new StringBuffer().append(this.adbConnection.getMaxData() - 9).append("isthemax").toString());
                mkid('D', 'A', 'T', 'A');
                long j = (long) 0;
                this.local.length();
                int i2 = 65536;
                int i3 = 16;
                int i4 = 8;
                int i5 = 0;
                Object obj = 1;
                while (true) {
                    byte[] copyOfRange;
                    int i6;
                    byte[] copyOfRange2;
                    log.a(new StringBuffer().append("loop=").append(i5).toString());
                    i5++;
                    byte[] bArr2 = (byte[]) null;
                    bArr2 = (byte[]) null;
                    bArr2 = (byte[]) null;
                    bArr2 = this.stream.read();
                    log.a(new StringBuffer().append(bArr2.length).append("islength").toString());
                    int i7 = i4;
                    j += (long) bArr2.length;
                    long j2 = (long) i2;
                    if (j > j2) {
                        long j3 = j;
                        if (j - j2 == ((long) bArr2.length)) {
                            copyOfRange = (bArr2.length < i7 || i7 <= 0) ? null : Arrays.copyOfRange(bArr2, 0, i7);
                            i6 = i7 + 8;
                            copyOfRange2 = bArr2.length > i3 ? Arrays.copyOfRange(bArr2, i3, bArr2.length) : null;
                            i3 += 8;
                            log.a(new StringBuffer().append(i6).append("is toone").toString());
                            log.a(new StringBuffer().append(i3).append("is fromtwo").toString());
                            if (i3 == 5004) {
                                i3 -= 4096;
                                i2 += 4096;
                            }
                            if (i6 >= 4096) {
                                i6 -= 4096;
                            }
                        } else {
                            i6 = i7;
                            copyOfRange = null;
                            copyOfRange2 = null;
                        }
                        StringBuffer stringBuffer = new StringBuffer();
                        StringBuffer append = new StringBuffer().append(new StringBuffer().append(bArr2.length).append("curlength").toString());
                        long j4 = j3;
                        log.a(stringBuffer.append(append.append(j4).toString()).append("cursent").toString());
                        j = j4 - ((long) i2);
                        log.a("skipdata");
                        i7 = i6;
                        i6 = i2;
                        bArr = null;
                    } else {
                        i6 = obj != null ? 8 : 0;
                        copyOfRange = null;
                        copyOfRange2 = null;
                        obj = null;
                        byte[] copyOfRange3 = Arrays.copyOfRange(bArr2, i6, bArr2.length);
                        i6 = i2;
                        bArr = copyOfRange3;
                    }
                    if (bArr == null || bArr.length != -1) {
                        if (copyOfRange2 != null && copyOfRange2.length == -1) {
                            break;
                        }
                        try {
                            bArr2 = (byte[]) null;
                            byte[] copyOfRange4 = (bArr == null || bArr.length <= 8) ? null : Arrays.copyOfRange(bArr, bArr.length - 8, bArr.length - 4);
                            if (copyOfRange != null && copyOfRange.length > 8) {
                                copyOfRange4 = Arrays.copyOfRange(copyOfRange, copyOfRange.length - 8, copyOfRange.length - 4);
                            }
                            byte[] copyOfRange5 = (copyOfRange2 == null || copyOfRange2.length <= 8) ? copyOfRange4 : Arrays.copyOfRange(copyOfRange2, copyOfRange2.length - 8, copyOfRange2.length - 4);
                            i = i6;
                            try {
                                String str4 = new String(copyOfRange5, Charset.forName("UTF-8"));
                                log.a(str4);
                                if (str4.equals("DONE")) {
                                    if (bArr == null || bArr.length <= 8) {
                                        if (copyOfRange != null && copyOfRange.length > 8) {
                                            fileOutputStream.write(copyOfRange, 0, copyOfRange.length - 8);
                                            break;
                                        } else if (copyOfRange2 != null) {
                                            try {
                                                if (copyOfRange2.length > 8) {
                                                    fileOutputStream.write(copyOfRange2, 0, copyOfRange2.length - 8);
                                                    break;
                                                }
                                            } catch (Exception e3) {
                                                e = e3;
                                                log.a(e);
                                                log.a(new StringBuffer().append("sended").append(i5).toString());
                                                i4 = i7;
                                                i2 = i;
                                            }
                                        }
                                    } else {
                                        fileOutputStream.write(bArr, 0, bArr.length - 8);
                                        break;
                                    }
                                }
                                if (bArr != null) {
                                    try {
                                        fileOutputStream.write(bArr, 0, bArr.length);
                                    } catch (Exception e4) {
                                        e = e4;
                                        log.a(e);
                                        log.a(new StringBuffer().append("sended").append(i5).toString());
                                        i4 = i7;
                                        i2 = i;
                                    }
                                }
                                if (copyOfRange != null) {
                                    fileOutputStream.write(copyOfRange, 0, copyOfRange.length);
                                }
                                if (copyOfRange2 != null) {
                                    try {
                                        fileOutputStream.write(copyOfRange2, 0, copyOfRange2.length);
                                    } catch (Exception e5) {
                                        e = e5;
                                    }
                                }
                            } catch (Exception e6) {
                                e = e6;
                                log.a(e);
                                log.a(new StringBuffer().append("sended").append(i5).toString());
                                i4 = i7;
                                i2 = i;
                            }
                        } catch (Exception e7) {
                            e = e7;
                            i = i6;
                            log.a(e);
                            log.a(new StringBuffer().append("sended").append(i5).toString());
                            i4 = i7;
                            i2 = i;
                        }
                        log.a(new StringBuffer().append("sended").append(i5).toString());
                        i4 = i7;
                        i2 = i;
                    } else {
                        break;
                    }
                }
                log.a("done");
                i2 = mkid('D', 'O', 'N', 'E');
                ByteBuffer allocate = ByteBuffer.allocate(8);
                log.a(0);
                allocate.order(ByteOrder.LITTLE_ENDIAN);
                allocate.putInt(i2);
                j = (long) 1000;
                allocate.putInt((int) (System.currentTimeMillis() / j));
                allocate.flip();
                log.a(1);
                log.a(2);
                i2 = mkid('O', 'K', 'A', 'Y');
                ByteBuffer allocate2 = ByteBuffer.allocate(8);
                log.a(6);
                allocate2.order(ByteOrder.LITTLE_ENDIAN);
                allocate2.putInt(i2);
                allocate2.putInt((int) (System.currentTimeMillis() / j));
                allocate2.flip();
                try {
                    log.a(3);
                } catch (Exception e22) {
                    log.a(e22);
                } catch (Throwable th) {
                    log.a(str);
                    throw th;
                }
                log.a(str);
            } catch (Exception e222) {
                log.a(e222);
            } catch (Throwable th2) {
                log.a(str2);
                //throw th2;
            }
            log.a(str2);
        }

        private final boolean readStatus(InputStream inputStream) throws IOException {
            try {
                AdbStream open = this.adbConnection.open("sync:");
                this.stream = open;
                String str = new String(Arrays.copyOf(open.read(), 4), Charset.forName("UTF-8"));
                log.a(str);
                if (str.equals("OKAY")) {
                    return true;
                }
                str.equals("FAIL");
                return false;
            } catch (Exception e) {
                log.a(e);
                return false;
            }
        }

        private final void sendRequest(AdbStream adbStream, int i, String str) throws IOException, InterruptedException {
            byte[] bytes = str.getBytes(Charset.forName("UTF-8"));
            ByteBuffer allocate = ByteBuffer.allocate(bytes.length + 8);
            allocate.order(ByteOrder.LITTLE_ENDIAN);
            allocate.putInt(i);
            allocate.putInt(bytes.length);
            allocate.put(bytes);
            allocate.flip();
            adbStream.write(allocate.array());
        }

        public Pullnew(Context context, AdbConnection adbConnection, File file, String str) {
            this.adbConnection = adbConnection;
            this.local = file;
            this.remotePath = str;
            this.mcon = context;
        }

        private final int mkid(char c, char c2, char c3, char c4) {
            return ((c | (c2 << 8)) | (c3 << 16)) | (c4 << 24);
        }
    }
}
