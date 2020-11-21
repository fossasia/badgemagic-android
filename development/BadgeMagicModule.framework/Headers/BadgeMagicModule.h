#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class BMMKotlinByteArray, BMMDataToSend, BMMMode, BMMSpeed, BMMConfigInfo, BMMMessage, BMMKotlinEnum, BMMBadgeConfig, BMMKotlinByteIterator, BMMKotlinArray, BMMKotlinx_serialization_runtimeSerialKind, BMMKotlinNothing, BMMKotlinx_serialization_runtimeUpdateMode;

@protocol BMMKotlinx_serialization_runtimeKSerializer, BMMKotlinComparable, BMMKotlinx_serialization_runtimeEncoder, BMMKotlinx_serialization_runtimeSerialDescriptor, BMMKotlinx_serialization_runtimeSerializationStrategy, BMMKotlinx_serialization_runtimeDecoder, BMMKotlinx_serialization_runtimeDeserializationStrategy, BMMKotlinIterator, BMMKotlinx_serialization_runtimeCompositeEncoder, BMMKotlinx_serialization_runtimeSerialModule, BMMKotlinAnnotation, BMMKotlinx_serialization_runtimeCompositeDecoder, BMMKotlinx_serialization_runtimeSerialModuleCollector, BMMKotlinKClass, BMMKotlinKDeclarationContainer, BMMKotlinKAnnotatedElement, BMMKotlinKClassifier;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wnullability"

@interface KotlinBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end;

@interface KotlinBase (KotlinBaseCopying) <NSCopying>
@end;

__attribute__((objc_runtime_name("KotlinMutableSet")))
__attribute__((swift_name("KotlinMutableSet")))
@interface BMMMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end;

__attribute__((objc_runtime_name("KotlinMutableDictionary")))
__attribute__((swift_name("KotlinMutableDictionary")))
@interface BMMMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end;

@interface NSError (NSErrorKotlinException)
@property (readonly) id _Nullable kotlinException;
@end;

__attribute__((objc_runtime_name("KotlinNumber")))
__attribute__((swift_name("KotlinNumber")))
@interface BMMNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end;

__attribute__((objc_runtime_name("KotlinByte")))
__attribute__((swift_name("KotlinByte")))
@interface BMMByte : BMMNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end;

__attribute__((objc_runtime_name("KotlinUByte")))
__attribute__((swift_name("KotlinUByte")))
@interface BMMUByte : BMMNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end;

__attribute__((objc_runtime_name("KotlinShort")))
__attribute__((swift_name("KotlinShort")))
@interface BMMShort : BMMNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end;

__attribute__((objc_runtime_name("KotlinUShort")))
__attribute__((swift_name("KotlinUShort")))
@interface BMMUShort : BMMNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end;

__attribute__((objc_runtime_name("KotlinInt")))
__attribute__((swift_name("KotlinInt")))
@interface BMMInt : BMMNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end;

__attribute__((objc_runtime_name("KotlinUInt")))
__attribute__((swift_name("KotlinUInt")))
@interface BMMUInt : BMMNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end;

__attribute__((objc_runtime_name("KotlinLong")))
__attribute__((swift_name("KotlinLong")))
@interface BMMLong : BMMNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end;

__attribute__((objc_runtime_name("KotlinULong")))
__attribute__((swift_name("KotlinULong")))
@interface BMMULong : BMMNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end;

__attribute__((objc_runtime_name("KotlinFloat")))
__attribute__((swift_name("KotlinFloat")))
@interface BMMFloat : BMMNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end;

__attribute__((objc_runtime_name("KotlinDouble")))
__attribute__((swift_name("KotlinDouble")))
@interface BMMDouble : BMMNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end;

__attribute__((objc_runtime_name("KotlinBoolean")))
__attribute__((swift_name("KotlinBoolean")))
@interface BMMBoolean : BMMNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DataToByteArrayConverter")))
@interface BMMDataToByteArrayConverter : KotlinBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)dataToByteArrayConverter __attribute__((swift_name("init()")));
- (NSArray<BMMKotlinByteArray *> *)convertData:(BMMDataToSend *)data __attribute__((swift_name("convert(data:)")));
@property (readonly) NSDictionary<id, NSString *> *CHAR_CODES __attribute__((swift_name("CHAR_CODES")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BadgeConfig")))
@interface BMMBadgeConfig : KotlinBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@property NSArray<NSString *> *hexStrings __attribute__((swift_name("hexStrings")));
@property BOOL isFlash __attribute__((swift_name("isFlash")));
@property BOOL isInverted __attribute__((swift_name("isInverted")));
@property BOOL isMarquee __attribute__((swift_name("isMarquee")));
@property BMMMode *mode __attribute__((swift_name("mode")));
@property BMMSpeed *speed __attribute__((swift_name("speed")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BadgeConfig.Companion")))
@interface BMMBadgeConfigCompanion : KotlinBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<BMMKotlinx_serialization_runtimeKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConfigInfo")))
@interface BMMConfigInfo : KotlinBase
- (instancetype)initWithBadgeJSON:(NSString *)badgeJSON fileName:(NSString *)fileName __attribute__((swift_name("init(badgeJSON:fileName:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (BMMConfigInfo *)doCopyBadgeJSON:(NSString *)badgeJSON fileName:(NSString *)fileName __attribute__((swift_name("doCopy(badgeJSON:fileName:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *badgeJSON __attribute__((swift_name("badgeJSON")));
@property (readonly) NSString *fileName __attribute__((swift_name("fileName")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DataToSend")))
@interface BMMDataToSend : KotlinBase
- (instancetype)initWithMessages:(NSArray<BMMMessage *> *)messages __attribute__((swift_name("init(messages:)"))) __attribute__((objc_designated_initializer));
- (NSArray<BMMMessage *> *)component1 __attribute__((swift_name("component1()")));
- (BMMDataToSend *)doCopyMessages:(NSArray<BMMMessage *> *)messages __attribute__((swift_name("doCopy(messages:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<BMMMessage *> *messages __attribute__((swift_name("messages")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DataToSend.Companion")))
@interface BMMDataToSendCompanion : KotlinBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<BMMKotlinx_serialization_runtimeKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Message")))
@interface BMMMessage : KotlinBase
- (instancetype)initWithHexStrings:(NSArray<NSString *> *)hexStrings flash:(BOOL)flash marquee:(BOOL)marquee speed:(BMMSpeed *)speed mode:(BMMMode *)mode __attribute__((swift_name("init(hexStrings:flash:marquee:speed:mode:)"))) __attribute__((objc_designated_initializer));
- (NSArray<NSString *> *)component1 __attribute__((swift_name("component1()")));
- (BOOL)component2 __attribute__((swift_name("component2()")));
- (BOOL)component3 __attribute__((swift_name("component3()")));
- (BMMSpeed *)component4 __attribute__((swift_name("component4()")));
- (BMMMode *)component5 __attribute__((swift_name("component5()")));
- (BMMMessage *)doCopyHexStrings:(NSArray<NSString *> *)hexStrings flash:(BOOL)flash marquee:(BOOL)marquee speed:(BMMSpeed *)speed mode:(BMMMode *)mode __attribute__((swift_name("doCopy(hexStrings:flash:marquee:speed:mode:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL flash __attribute__((swift_name("flash")));
@property (readonly) NSArray<NSString *> *hexStrings __attribute__((swift_name("hexStrings")));
@property (readonly) BOOL marquee __attribute__((swift_name("marquee")));
@property (readonly) BMMMode *mode __attribute__((swift_name("mode")));
@property (readonly) BMMSpeed *speed __attribute__((swift_name("speed")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Message.Companion")))
@interface BMMMessageCompanion : KotlinBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<BMMKotlinx_serialization_runtimeKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("KotlinComparable")))
@protocol BMMKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end;

__attribute__((swift_name("KotlinEnum")))
@interface BMMKotlinEnum : KotlinBase <BMMKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
- (int32_t)compareToOther:(BMMKotlinEnum *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Mode")))
@interface BMMMode : BMMKotlinEnum
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) BMMMode *left __attribute__((swift_name("left")));
@property (class, readonly) BMMMode *right __attribute__((swift_name("right")));
@property (class, readonly) BMMMode *up __attribute__((swift_name("up")));
@property (class, readonly) BMMMode *down __attribute__((swift_name("down")));
@property (class, readonly) BMMMode *fixed __attribute__((swift_name("fixed")));
@property (class, readonly) BMMMode *snowflake __attribute__((swift_name("snowflake")));
@property (class, readonly) BMMMode *picture __attribute__((swift_name("picture")));
@property (class, readonly) BMMMode *animation __attribute__((swift_name("animation")));
@property (class, readonly) BMMMode *laser __attribute__((swift_name("laser")));
- (int32_t)compareToOther:(BMMMode *)other __attribute__((swift_name("compareTo(other:)")));
@property (readonly) int8_t hexValue __attribute__((swift_name("hexValue")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Speed")))
@interface BMMSpeed : BMMKotlinEnum
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) BMMSpeed *one __attribute__((swift_name("one")));
@property (class, readonly) BMMSpeed *two __attribute__((swift_name("two")));
@property (class, readonly) BMMSpeed *three __attribute__((swift_name("three")));
@property (class, readonly) BMMSpeed *four __attribute__((swift_name("four")));
@property (class, readonly) BMMSpeed *five __attribute__((swift_name("five")));
@property (class, readonly) BMMSpeed *six __attribute__((swift_name("six")));
@property (class, readonly) BMMSpeed *seven __attribute__((swift_name("seven")));
@property (class, readonly) BMMSpeed *eight __attribute__((swift_name("eight")));
- (int32_t)compareToOther:(BMMSpeed *)other __attribute__((swift_name("compareTo(other:)")));
@property (readonly) int8_t hexValue __attribute__((swift_name("hexValue")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("JSONHelper")))
@interface BMMJSONHelper : KotlinBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)jSONHelper __attribute__((swift_name("init()")));
- (BMMBadgeConfig *)decodeJSONBadgeJSON:(NSString *)badgeJSON __attribute__((swift_name("decodeJSON(badgeJSON:)")));
- (NSString *)encodeJSONBadgeData:(BMMBadgeConfig *)badgeData __attribute__((swift_name("encodeJSON(badgeData:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ByteArrayUtils")))
@interface BMMByteArrayUtils : KotlinBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)byteArrayUtils __attribute__((swift_name("init()")));
- (NSString *)byteArrayToHexStringByteArray:(BMMKotlinByteArray *)byteArray __attribute__((swift_name("byteArrayToHexString(byteArray:)")));
- (BMMKotlinByteArray *)hexStringToByteArrayHexString:(NSString *)hexString __attribute__((swift_name("hexStringToByteArray(hexString:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BadgeConfigKt")))
@interface BMMBadgeConfigKt : KotlinBase
@property (class, readonly) NSString *CONF_FLASH __attribute__((swift_name("CONF_FLASH")));
@property (class, readonly) NSString *CONF_HEX_STRINGS __attribute__((swift_name("CONF_HEX_STRINGS")));
@property (class, readonly) NSString *CONF_INVERTED __attribute__((swift_name("CONF_INVERTED")));
@property (class, readonly) NSString *CONF_MARQUEE __attribute__((swift_name("CONF_MARQUEE")));
@property (class, readonly) NSString *CONF_MODE __attribute__((swift_name("CONF_MODE")));
@property (class, readonly) NSString *CONF_SPEED __attribute__((swift_name("CONF_SPEED")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinByteArray")))
@interface BMMKotlinByteArray : KotlinBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(BMMByte *(^)(BMMInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int8_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (BMMKotlinByteIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int8_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeSerializationStrategy")))
@protocol BMMKotlinx_serialization_runtimeSerializationStrategy
@required
- (void)serializeEncoder:(id<BMMKotlinx_serialization_runtimeEncoder>)encoder obj:(id _Nullable)obj __attribute__((swift_name("serialize(encoder:obj:)")));
@property (readonly) id<BMMKotlinx_serialization_runtimeSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeDeserializationStrategy")))
@protocol BMMKotlinx_serialization_runtimeDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<BMMKotlinx_serialization_runtimeDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (id _Nullable)patchDecoder:(id<BMMKotlinx_serialization_runtimeDecoder>)decoder old:(id _Nullable)old __attribute__((swift_name("patch(decoder:old:)")));
@property (readonly) id<BMMKotlinx_serialization_runtimeSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeKSerializer")))
@protocol BMMKotlinx_serialization_runtimeKSerializer <BMMKotlinx_serialization_runtimeSerializationStrategy, BMMKotlinx_serialization_runtimeDeserializationStrategy>
@required
@end;

__attribute__((swift_name("KotlinIterator")))
@protocol BMMKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end;

__attribute__((swift_name("KotlinByteIterator")))
@interface BMMKotlinByteIterator : KotlinBase <BMMKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (BMMByte *)next __attribute__((swift_name("next()")));
- (int8_t)nextByte __attribute__((swift_name("nextByte()")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeEncoder")))
@protocol BMMKotlinx_serialization_runtimeEncoder
@required
- (id<BMMKotlinx_serialization_runtimeCompositeEncoder>)beginCollectionDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc collectionSize:(int32_t)collectionSize typeParams:(BMMKotlinArray *)typeParams __attribute__((swift_name("beginCollection(desc:collectionSize:typeParams:)")));
- (id<BMMKotlinx_serialization_runtimeCompositeEncoder>)beginStructureDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc typeParams:(BMMKotlinArray *)typeParams __attribute__((swift_name("beginStructure(desc:typeParams:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescription:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)enumDescription ordinal:(int32_t)ordinal __attribute__((swift_name("encodeEnum(enumDescription:ordinal:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (void)encodeIntValue:(int32_t)value __attribute__((swift_name("encodeInt(value:)")));
- (void)encodeLongValue:(int64_t)value __attribute__((swift_name("encodeLong(value:)")));
- (void)encodeNotNullMark __attribute__((swift_name("encodeNotNullMark()")));
- (void)encodeNull __attribute__((swift_name("encodeNull()")));
- (void)encodeNullableSerializableValueSerializer:(id<BMMKotlinx_serialization_runtimeSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<BMMKotlinx_serialization_runtimeSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
- (void)encodeUnit __attribute__((swift_name("encodeUnit()")));
@property (readonly) id<BMMKotlinx_serialization_runtimeSerialModule> context __attribute__((swift_name("context")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeSerialDescriptor")))
@protocol BMMKotlinx_serialization_runtimeSerialDescriptor
@required
- (NSArray<id<BMMKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));
- (id<BMMKotlinx_serialization_runtimeSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));
- (int32_t)getElementIndexName:(NSString *)name __attribute__((swift_name("getElementIndex(name:)")));
- (NSString *)getElementNameIndex:(int32_t)index __attribute__((swift_name("getElementName(index:)")));
- (NSArray<id<BMMKotlinAnnotation>> *)getEntityAnnotations __attribute__((swift_name("getEntityAnnotations()")));
- (BOOL)isElementOptionalIndex:(int32_t)index __attribute__((swift_name("isElementOptional(index:)")));
@property (readonly) int32_t elementsCount __attribute__((swift_name("elementsCount")));
@property (readonly) BOOL isNullable __attribute__((swift_name("isNullable")));
@property (readonly) BMMKotlinx_serialization_runtimeSerialKind *kind __attribute__((swift_name("kind")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeDecoder")))
@protocol BMMKotlinx_serialization_runtimeDecoder
@required
- (id<BMMKotlinx_serialization_runtimeCompositeDecoder>)beginStructureDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc typeParams:(BMMKotlinArray *)typeParams __attribute__((swift_name("beginStructure(desc:typeParams:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescription:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)enumDescription __attribute__((swift_name("decodeEnum(enumDescription:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (int32_t)decodeInt __attribute__((swift_name("decodeInt()")));
- (int64_t)decodeLong __attribute__((swift_name("decodeLong()")));
- (BOOL)decodeNotNullMark __attribute__((swift_name("decodeNotNullMark()")));
- (BMMKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<BMMKotlinx_serialization_runtimeDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<BMMKotlinx_serialization_runtimeDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
- (void)decodeUnit __attribute__((swift_name("decodeUnit()")));
- (id _Nullable)updateNullableSerializableValueDeserializer:(id<BMMKotlinx_serialization_runtimeDeserializationStrategy>)deserializer old:(id _Nullable)old __attribute__((swift_name("updateNullableSerializableValue(deserializer:old:)")));
- (id _Nullable)updateSerializableValueDeserializer:(id<BMMKotlinx_serialization_runtimeDeserializationStrategy>)deserializer old:(id _Nullable)old __attribute__((swift_name("updateSerializableValue(deserializer:old:)")));
@property (readonly) id<BMMKotlinx_serialization_runtimeSerialModule> context __attribute__((swift_name("context")));
@property (readonly) BMMKotlinx_serialization_runtimeUpdateMode *updateMode __attribute__((swift_name("updateMode")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeCompositeEncoder")))
@protocol BMMKotlinx_serialization_runtimeCompositeEncoder
@required
- (void)encodeBooleanElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(desc:index:value:)")));
- (void)encodeByteElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(desc:index:value:)")));
- (void)encodeCharElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(desc:index:value:)")));
- (void)encodeDoubleElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(desc:index:value:)")));
- (void)encodeFloatElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(desc:index:value:)")));
- (void)encodeIntElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(desc:index:value:)")));
- (void)encodeLongElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(desc:index:value:)")));
- (void)encodeNonSerializableElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index value:(id)value __attribute__((swift_name("encodeNonSerializableElement(desc:index:value:)")));
- (void)encodeNullableSerializableElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index serializer:(id<BMMKotlinx_serialization_runtimeSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(desc:index:serializer:value:)")));
- (void)encodeSerializableElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index serializer:(id<BMMKotlinx_serialization_runtimeSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(desc:index:serializer:value:)")));
- (void)encodeShortElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(desc:index:value:)")));
- (void)encodeStringElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(desc:index:value:)")));
- (void)encodeUnitElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("encodeUnitElement(desc:index:)")));
- (void)endStructureDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc __attribute__((swift_name("endStructure(desc:)")));
- (BOOL)shouldEncodeElementDefaultDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(desc:index:)")));
@property (readonly) id<BMMKotlinx_serialization_runtimeSerialModule> context __attribute__((swift_name("context")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface BMMKotlinArray : KotlinBase
+ (instancetype)arrayWithSize:(int32_t)size init:(id _Nullable (^)(BMMInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (id _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<BMMKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(id _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeSerialModule")))
@protocol BMMKotlinx_serialization_runtimeSerialModule
@required
- (void)dumpToCollector:(id<BMMKotlinx_serialization_runtimeSerialModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));
- (id<BMMKotlinx_serialization_runtimeKSerializer> _Nullable)getContextualKclass:(id<BMMKotlinKClass>)kclass __attribute__((swift_name("getContextual(kclass:)")));
- (id<BMMKotlinx_serialization_runtimeKSerializer> _Nullable)getPolymorphicBaseClass:(id<BMMKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));
- (id<BMMKotlinx_serialization_runtimeKSerializer> _Nullable)getPolymorphicBaseClass:(id<BMMKotlinKClass>)baseClass serializedClassName:(NSString *)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end;

__attribute__((swift_name("KotlinAnnotation")))
@protocol BMMKotlinAnnotation
@required
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeSerialKind")))
@interface BMMKotlinx_serialization_runtimeSerialKind : KotlinBase
- (NSString *)description __attribute__((swift_name("description()")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeCompositeDecoder")))
@protocol BMMKotlinx_serialization_runtimeCompositeDecoder
@required
- (BOOL)decodeBooleanElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(desc:index:)")));
- (int8_t)decodeByteElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("decodeByteElement(desc:index:)")));
- (unichar)decodeCharElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("decodeCharElement(desc:index:)")));
- (int32_t)decodeCollectionSizeDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc __attribute__((swift_name("decodeCollectionSize(desc:)")));
- (double)decodeDoubleElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(desc:index:)")));
- (int32_t)decodeElementIndexDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc __attribute__((swift_name("decodeElementIndex(desc:)")));
- (float)decodeFloatElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("decodeFloatElement(desc:index:)")));
- (int32_t)decodeIntElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("decodeIntElement(desc:index:)")));
- (int64_t)decodeLongElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("decodeLongElement(desc:index:)")));
- (id _Nullable)decodeNullableSerializableElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index deserializer:(id<BMMKotlinx_serialization_runtimeDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableElement(desc:index:deserializer:)")));
- (id _Nullable)decodeSerializableElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index deserializer:(id<BMMKotlinx_serialization_runtimeDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableElement(desc:index:deserializer:)")));
- (int16_t)decodeShortElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("decodeShortElement(desc:index:)")));
- (NSString *)decodeStringElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("decodeStringElement(desc:index:)")));
- (void)decodeUnitElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index __attribute__((swift_name("decodeUnitElement(desc:index:)")));
- (void)endStructureDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc __attribute__((swift_name("endStructure(desc:)")));
- (id _Nullable)updateNullableSerializableElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index deserializer:(id<BMMKotlinx_serialization_runtimeDeserializationStrategy>)deserializer old:(id _Nullable)old __attribute__((swift_name("updateNullableSerializableElement(desc:index:deserializer:old:)")));
- (id _Nullable)updateSerializableElementDesc:(id<BMMKotlinx_serialization_runtimeSerialDescriptor>)desc index:(int32_t)index deserializer:(id<BMMKotlinx_serialization_runtimeDeserializationStrategy>)deserializer old:(id _Nullable)old __attribute__((swift_name("updateSerializableElement(desc:index:deserializer:old:)")));
@property (readonly) id<BMMKotlinx_serialization_runtimeSerialModule> context __attribute__((swift_name("context")));
@property (readonly) BMMKotlinx_serialization_runtimeUpdateMode *updateMode __attribute__((swift_name("updateMode")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface BMMKotlinNothing : KotlinBase
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_runtimeUpdateMode")))
@interface BMMKotlinx_serialization_runtimeUpdateMode : BMMKotlinEnum
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) BMMKotlinx_serialization_runtimeUpdateMode *banned __attribute__((swift_name("banned")));
@property (class, readonly) BMMKotlinx_serialization_runtimeUpdateMode *overwrite __attribute__((swift_name("overwrite")));
@property (class, readonly) BMMKotlinx_serialization_runtimeUpdateMode *update __attribute__((swift_name("update")));
- (int32_t)compareToOther:(BMMKotlinx_serialization_runtimeUpdateMode *)other __attribute__((swift_name("compareTo(other:)")));
@end;

__attribute__((swift_name("Kotlinx_serialization_runtimeSerialModuleCollector")))
@protocol BMMKotlinx_serialization_runtimeSerialModuleCollector
@required
- (void)contextualKClass:(id<BMMKotlinKClass>)kClass serializer:(id<BMMKotlinx_serialization_runtimeKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<BMMKotlinKClass>)baseClass actualClass:(id<BMMKotlinKClass>)actualClass actualSerializer:(id<BMMKotlinx_serialization_runtimeKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
@end;

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol BMMKotlinKDeclarationContainer
@required
@end;

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol BMMKotlinKAnnotatedElement
@required
@end;

__attribute__((swift_name("KotlinKClassifier")))
@protocol BMMKotlinKClassifier
@required
@end;

__attribute__((swift_name("KotlinKClass")))
@protocol BMMKotlinKClass <BMMKotlinKDeclarationContainer, BMMKotlinKAnnotatedElement, BMMKotlinKClassifier>
@required
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end;

#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
