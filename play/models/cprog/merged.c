# 3 "tut2.c"
struct Irp {
   int Status ;
   int Information ;
};
# 8
struct Requests {
   int Status ;
   struct Irp *irp ;
   struct Requests *Next ;
};
# 14
struct Device {
   struct Requests *WriteListHeadVa ;
   int writeListLock ;
};
# 1
int STATUS_SUCCESS   = 0;
# 2
int STATUS_UNSUCCESSFUL   = -1;
# 19
void FSMInit(void) 
{ 

  {
# 19
  return;
}
}
# 23
void FSMLock(void) 
{ 

  {
# 23
  return;
}
}
# 27
void FSMUnLock(void) 
{ 

  {
# 27
  return;
}
}
# 31
void SmartDevFreeBlock(struct Requests *r ) 
{ 

  {
# 31
  return;
}
}
# 35
void IoCompleteRequest(struct Irp *irp , int status ) 
{ 

  {
# 35
  return;
}
}
# 39
struct Device devE  ;
# 41
void main(void) 
{ int IO_NO_INCREMENT ;
  int nPacketsOld ;
  int nPackets ;
  struct Requests *request ;
  struct Irp *irp ;
  struct Device *devExt ;

  {
# 42
  IO_NO_INCREMENT = 3;
# 48
  FSMInit();
# 49
  devExt = & devE;
# 53
  while (1) {
# 54
    FSMLock();
# 55
    nPacketsOld = nPackets;
# 57
    request = devExt->WriteListHeadVa;
# 59
    if (request != (struct Requests *)0) {
# 59
      if (request->Status != 0) {
# 60
        devExt->WriteListHeadVa = request->Next;
# 62
        FSMUnLock();
# 63
        irp = request->irp;
# 65
        if (request->Status > 0) {
# 66
          irp->Status = STATUS_SUCCESS;
# 67
          irp->Information = request->Status;
        } else {
# 69
          irp->Status = STATUS_UNSUCCESSFUL;
# 70
          irp->Information = request->Status;
        }
# 72
        SmartDevFreeBlock(request);
# 73
        IO_NO_INCREMENT = 3;
# 74
        IoCompleteRequest(irp, IO_NO_INCREMENT);
# 75
        nPackets = nPackets;
      }
    }
# 53
    if (! (nPackets != nPacketsOld)) {
# 53
      break;
    }
  }
# 78
  FSMUnLock();
# 78
  return;
}
}
