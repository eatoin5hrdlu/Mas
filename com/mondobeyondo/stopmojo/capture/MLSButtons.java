package com.mondobeyondo.stopmojo.capture;
//---------------------------------------------------------------------------
//	using the add/go/get method
//	Records the time for 1000 iterations and divides by 1000,
//	verification of the basic command/response communication

import com.labjack.labjackud.*; // importing the LJUD class

public class MLSButtons {

    private int[] intHandle={0};
	private	int   intErrorcode;
	private static boolean labjackOpen = false;


	private 		long quickSample = 0;  //Set to TRUE for quick AIN sampling.
	private 		long longSettling = 1;  //Set to TRUE for extra AIN settling time.

	
	public static boolean labjackInstalled()
	{
		return labjackOpen;
	}
		
	public MLSButtons() {
		    	//System.out.println("Button Constructor");
				try 
					{
						System.loadLibrary("LJUDJava");
					}
				catch (UnsatisfiedLinkError e)
					{
						System.err.println("Library load failed.\n" + e);
						System.exit(1);
					}

			//Open the first found LabJack.
				try 
				{
					intErrorcode = LJUD.OpenLabJack (LJUD.LJ_dtU3, LJUD.LJ_ctUSB, "1", 1, intHandle);
					if (intErrorcode == LJUD.LJE_NOERROR)
					{
						labjackOpen = true;
					}
					else
					{
						labjackOpen = false;
						return;
					}
					ErrorHandler(intErrorcode, 0, new Exception());
				} catch(Exception elj) {
					labjackOpen = false;
					return;
				}
			// factory default condition.
			intErrorcode = LJUD.ePut (intHandle[0], LJUD.LJ_ioPIN_CONFIGURATION_RESET, 0, 0, 0);
			ErrorHandler(intErrorcode, 0, new Exception());

			//Configure quickSample.
			intErrorcode = LJUD.ePut (intHandle[0], LJUD.LJ_ioPUT_CONFIG, LJUD.LJ_chAIN_RESOLUTION, quickSample, 0);
			ErrorHandler(intErrorcode, 0, new Exception());

			//Configure longSettling.
			intErrorcode = LJUD.ePut (intHandle[0], LJUD.LJ_ioPUT_CONFIG, LJUD.LJ_chAIN_SETTLING_TIME, longSettling, 0);
			ErrorHandler(intErrorcode, 0, new Exception());
		
			//Configure the necessary lines as analog.
			intErrorcode = LJUD.ePut (intHandle[0], LJUD.LJ_ioPUT_ANALOG_ENABLE_PORT, 0, 0, 16);
			ErrorHandler(intErrorcode, 0, new Exception());
		 //Now add requests that will be processed every iteration of the loop.
		 //Read CIO digital lines.
			intErrorcode = LJUD.AddRequest (intHandle[0], LJUD.LJ_ioGET_DIGITAL_PORT, 0, 0, 9, 0);
			ErrorHandler(intErrorcode, 0, new Exception());		
    }

	public static void ErrorHandler(int Errorcode, long Iteration, Exception excep) {
		byte[] err = new byte[255];

		if (Errorcode != LJUD.LJE_NOERROR) {
			LJUD.ErrorToString(Errorcode, err);
			System.out.println("Error number = " + Errorcode);
			System.out.println("Error string = " + new String(err));
			System.out.println("Iteration = " + Iteration);
			System.out.println("Stack Trace : ");
			excep.printStackTrace();
			if (Errorcode > LJUD.LJE_MIN_GROUP_ERROR) {
				// Quit if this is a group error.
				System.exit(1);
			}
		}
	}


	public int getButtons() throws Exception {
		int[] intIOType={0};
		int[] intChannel={0};
		double[] dblValue={0};
		// Variables to satisfy certain method signatures
		int[] dummyInt = {0};
		double[] dummyDouble = {0};
		long lngIteration;
		int intErrorcode;
		int ValueDIPort = 0;

	    intErrorcode = LJUD.GoOne(intHandle[0]);
	    ErrorHandler(intErrorcode, 0, new Exception());

	    intErrorcode = LJUD.GetFirstResult(intHandle[0], intIOType, intChannel, dblValue, dummyInt, dummyDouble);
	    ErrorHandler(intErrorcode, 0, new Exception());
	    lngIteration=0;	//Used by the error handling function.
	    while(intErrorcode < LJUD.LJE_MIN_GROUP_ERROR)
		{
		    if (intIOType[0] == LJUDJavaConstants.LJ_ioGET_DIGITAL_PORT)
			{
			    ValueDIPort = (int) dblValue[0];
			}
		    
		    intErrorcode = LJUD.GetNextResult(intHandle[0], intIOType, intChannel, dblValue, dummyInt, dummyDouble);
		    
		    if(intErrorcode != LJUD.LJE_NO_MORE_DATA_AVAILABLE)
			{
			    ErrorHandler(intErrorcode, lngIteration, new Exception());
			}
		    lngIteration++;
		}
	    return ValueDIPort;
	}

    

	/*

    public static void main(String[] args) {
	    MLSButtons u3 = null;
	    int res = 42;

		try {
		    u3 = new MLSButtons();
		    u3.setupButtons();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		while(true &&  u3 != null)
		    {
			try {
			    res = u3.getButtons();
			} catch (Exception ex) {
			    ex.printStackTrace();
			}
			System.out.print("V "+Integer.toBinaryString(res|0x20)+"\r");
			mysleep(100);
		    }
	}
	*/
    
}