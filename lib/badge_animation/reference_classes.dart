class IntReference {
  int _value;
  IntReference(this._value);

  int get value => _value;
  set value(int newValue) {
    _value = newValue;
  }
}
