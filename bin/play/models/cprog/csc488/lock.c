
void __error__(void) 
{ 

  {
ERROR: 
    goto ERROR;
  }
}

int lockStatus   = 0;

void __initialize__(void) 
{ 

  {

    lockStatus = 0;

    STATUS_SUCCESS = 0;

    STATUS_UNSUCCESSFUL = -1;

    return;
  }
}

struct Irp {
  int Status ;
  int Information ;
};

struct Requests {
  int Status ;
  struct Irp *irp ;
  struct Requests *Next ;
};

struct Device {
  struct Requests *WriteListHeadVa ;
  int writeListLock ;
};

int STATUS_SUCCESS   = 0;

int STATUS_UNSUCCESSFUL   = -1;

void FSMInit(void) 
{ 

  {

    return;
  }
}

void FSMLock(void) 
{ 

  {

LOCK:    return;
  }
}

void FSMUnLock(void) 
{ 

  {

UNLOCK:    return;
  }
}

void SmartDevFreeBlock(struct Requests *r ) 
{ 

  {

    return;
  }
}

void IoCompleteRequest(struct Irp *irp , int status ) 
{ 

  {

    return;
  }
}

struct Device devE  ;

void main(void) 
{
  int IO_NO_INCREMENT ;
  int nPacketsOld ;
  int nPackets ;
  struct Requests *request ;
  struct Irp *irp ;
  struct Device *devExt ;

  {

    __initialize__();
    {

      IO_NO_INCREMENT = 3;
      {

        lockStatus = 0;
        {

        }
      }

      FSMInit();

    }

    while (1 == 1) {
      {

        {

          if (lockStatus == 0) {

            lockStatus = 1;
          } else {

ERROR1:            __error__();
          }
          {

          }
        }

        FSMLock(); // *******************************************************

        nPacketsOld = nPackets;

        request = devExt->WriteListHeadVa;
      }

      if (request != 0) {

        if (request != 0) {
          {

            devExt->WriteListHeadVa = request->Next;
            if (lockStatus == 1) {
              lockStatus = 0;
            } else {
ERROR2:   __error__();
            }


            FSMUnLock(); // *************************************************

            irp = request->irp;
          }

          if (request > 0) {
            {

              irp->Status = STATUS_SUCCESS;

              irp->Information = request->Status;
            }
          } else {
            {

              irp->Status = STATUS_UNSUCCESSFUL;

              irp->Information = request->Status;
            }
          }
          {

            SmartDevFreeBlock(request);

            IO_NO_INCREMENT = 3;

            IoCompleteRequest(irp, IO_NO_INCREMENT);

            nPackets = nPackets + 1;
          }
        }
      }

      if ( (nPackets == nPacketsOld)) {

        break;
      }
    }
    {
      if (lockStatus == 1) {
        lockStatus = 0;
      } else {
ERROR3: __error__();
      }


      FSMUnLock(); // *******************************************************
    }

    return;
  }
}

