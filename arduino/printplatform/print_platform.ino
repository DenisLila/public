#if defined(__AVR__)
char *platform = "avr";
#elif defined(__MK20DX128__)
char *platform = "mk20dx";
#elif defined(__SAM3X8E__)
char *platform = "sam3x8e";
#elif defined(__PIC32MX__)
char *platform = "pic32mx";
#else
char *platform = "undefined";
#endif

void setup() {
  Serial.begin(9600);
}

void loop() {
  Serial.write(platform);
  delay(1000);
}
