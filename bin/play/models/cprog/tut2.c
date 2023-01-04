int STATUS_SUCCESS = 0;
int STATUS_UNSUCCESSFUL = -1;
struct Irp {
  int Status;
  int Information;
};

struct Requests {
  int Status;
  struct Irp *irp;
  struct Requests *Next;	
};

struct Device {
  struct Requests *WriteListHeadVa; 
  int writeListLock;
};

void FSMInit() {
// code to initialize the global lock to unlocked state
}

void FSMLock() {
// code for acquiring the lock

}
void FSMUnLock() {
// code for releasing the lock
}

void SmartDevFreeBlock(struct Requests *r) {
// code omitted for simplicity
}

void IoCompleteRequest(struct Irp *irp, int status) {
// code omitted for simplicity
}

struct Device devE;

void main () {
 int IO_NO_INCREMENT = 3;
 int nPacketsOld, nPackets;
 struct Requests *request;
 struct Irp *irp;
 struct Device *devExt;

 FSMInit();
 devExt = &devE;	


 /* driver code */
 do {
   FSMLock();
   nPacketsOld = nPackets; 
	    
   request = devExt->WriteListHeadVa;
	    
   if(request!=0 && request->Status!=0){
     devExt->WriteListHeadVa = request->Next;	
		
     FSMUnLock();
     irp = request->irp;
		
     if((*request).Status >0) {
       (*irp).Status = STATUS_SUCCESS;
       (*irp).Information = (*request).Status;
     } else {
       (*irp).Status = STATUS_UNSUCCESSFUL;
       (*irp).Information = (*request).Status;
     }	
     SmartDevFreeBlock(request);
     IO_NO_INCREMENT = 3;
     IoCompleteRequest(irp, IO_NO_INCREMENT);
     nPackets = nPackets + 1;
   }
 } while (nPackets != nPacketsOld);
 FSMUnLock();
}
