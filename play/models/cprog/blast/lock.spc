global int lockStatus = 0;

event {
  pattern { FSMInit(); }
  action { lockStatus = 0; }
}

event {
  pattern { FSMLock(); }
  guard { lockStatus == 0 }
  action { lockStatus = 1; }
}

event {
  pattern { FSMUnlock(); }
  guard { lockStatus == 1 }
  action { lockStatus = 0; }
}
