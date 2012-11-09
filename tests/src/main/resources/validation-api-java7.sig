#Signature file v4.0
#Version 

CLSS public abstract interface java.io.Serializable

CLSS public abstract interface java.lang.Comparable<%0 extends java.lang.Object>
meth public abstract int compareTo({java.lang.Comparable%0})

CLSS public abstract java.lang.Enum<%0 extends java.lang.Enum<{java.lang.Enum%0}>>
cons protected Enum(java.lang.String,int)
intf java.io.Serializable
intf java.lang.Comparable<{java.lang.Enum%0}>
meth protected final java.lang.Object clone() throws java.lang.CloneNotSupportedException
meth protected final void finalize()
meth public final boolean equals(java.lang.Object)
meth public final int compareTo({java.lang.Enum%0})
meth public final int hashCode()
meth public final int ordinal()
meth public final java.lang.Class<{java.lang.Enum%0}> getDeclaringClass()
meth public final java.lang.String name()
meth public java.lang.String toString()
meth public static <%0 extends java.lang.Enum<{%%0}>> {%%0} valueOf(java.lang.Class<{%%0}>,java.lang.String)
supr java.lang.Object
hfds name,ordinal

CLSS public java.lang.Exception
cons protected Exception(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public Exception()
cons public Exception(java.lang.String)
cons public Exception(java.lang.String,java.lang.Throwable)
cons public Exception(java.lang.Throwable)
supr java.lang.Throwable
hfds serialVersionUID

CLSS public abstract interface java.lang.Iterable<%0 extends java.lang.Object>
meth public abstract java.util.Iterator<{java.lang.Iterable%0}> iterator()

CLSS public java.lang.Object
cons public Object()
meth protected java.lang.Object clone() throws java.lang.CloneNotSupportedException
meth protected void finalize() throws java.lang.Throwable
meth public boolean equals(java.lang.Object)
meth public final java.lang.Class<?> getClass()
meth public final void notify()
meth public final void notifyAll()
meth public final void wait() throws java.lang.InterruptedException
meth public final void wait(long) throws java.lang.InterruptedException
meth public final void wait(long,int) throws java.lang.InterruptedException
meth public int hashCode()
meth public java.lang.String toString()

CLSS public java.lang.RuntimeException
cons protected RuntimeException(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public RuntimeException()
cons public RuntimeException(java.lang.String)
cons public RuntimeException(java.lang.String,java.lang.Throwable)
cons public RuntimeException(java.lang.Throwable)
supr java.lang.Exception
hfds serialVersionUID

CLSS public java.lang.Throwable
cons protected Throwable(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public Throwable()
cons public Throwable(java.lang.String)
cons public Throwable(java.lang.String,java.lang.Throwable)
cons public Throwable(java.lang.Throwable)
intf java.io.Serializable
meth public final java.lang.Throwable[] getSuppressed()
meth public final void addSuppressed(java.lang.Throwable)
meth public java.lang.StackTraceElement[] getStackTrace()
meth public java.lang.String getLocalizedMessage()
meth public java.lang.String getMessage()
meth public java.lang.String toString()
meth public java.lang.Throwable fillInStackTrace()
meth public java.lang.Throwable getCause()
meth public java.lang.Throwable initCause(java.lang.Throwable)
meth public void printStackTrace()
meth public void printStackTrace(java.io.PrintStream)
meth public void printStackTrace(java.io.PrintWriter)
meth public void setStackTrace(java.lang.StackTraceElement[])
supr java.lang.Object
hfds CAUSE_CAPTION,EMPTY_THROWABLE_ARRAY,NULL_CAUSE_MESSAGE,SELF_SUPPRESSION_MESSAGE,SUPPRESSED_CAPTION,SUPPRESSED_SENTINEL,UNASSIGNED_STACK,backtrace,cause,detailMessage,serialVersionUID,stackTrace,suppressedExceptions
hcls PrintStreamOrWriter,SentinelHolder,WrappedPrintStream,WrappedPrintWriter

CLSS public abstract interface java.lang.annotation.Annotation
meth public abstract boolean equals(java.lang.Object)
meth public abstract int hashCode()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> annotationType()
meth public abstract java.lang.String toString()

CLSS public abstract interface !annotation java.lang.annotation.Documented
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation java.lang.annotation.Retention
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.annotation.RetentionPolicy value()

CLSS public abstract interface !annotation java.lang.annotation.Target
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.annotation.ElementType[] value()

CLSS public abstract interface javax.validation.BootstrapConfiguration
meth public abstract java.lang.String getConstraintValidatorFactoryClassName()
meth public abstract java.lang.String getDefaultProviderClassName()
meth public abstract java.lang.String getMessageInterpolatorClassName()
meth public abstract java.lang.String getParameterNameProviderClassName()
meth public abstract java.lang.String getTraversableResolverClassName()
meth public abstract java.util.Map<java.lang.String,java.lang.String> getProperties()
meth public abstract java.util.Set<java.lang.String> getConstraintMappingResourcePaths()

CLSS public abstract interface javax.validation.Configuration<%0 extends javax.validation.Configuration<{javax.validation.Configuration%0}>>
meth public abstract javax.validation.BootstrapConfiguration getBootstrapConfiguration()
meth public abstract javax.validation.ConstraintValidatorFactory getDefaultConstraintValidatorFactory()
meth public abstract javax.validation.MessageInterpolator getDefaultMessageInterpolator()
meth public abstract javax.validation.ParameterNameProvider getDefaultParameterNameProvider()
meth public abstract javax.validation.TraversableResolver getDefaultTraversableResolver()
meth public abstract javax.validation.ValidatorFactory buildValidatorFactory()
meth public abstract {javax.validation.Configuration%0} addMapping(java.io.InputStream)
meth public abstract {javax.validation.Configuration%0} addProperty(java.lang.String,java.lang.String)
meth public abstract {javax.validation.Configuration%0} constraintValidatorFactory(javax.validation.ConstraintValidatorFactory)
meth public abstract {javax.validation.Configuration%0} ignoreXmlConfiguration()
meth public abstract {javax.validation.Configuration%0} messageInterpolator(javax.validation.MessageInterpolator)
meth public abstract {javax.validation.Configuration%0} parameterNameProvider(javax.validation.ParameterNameProvider)
meth public abstract {javax.validation.Configuration%0} traversableResolver(javax.validation.TraversableResolver)

CLSS public abstract interface !annotation javax.validation.Constraint
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy()

CLSS public javax.validation.ConstraintDeclarationException
cons public ConstraintDeclarationException()
cons public ConstraintDeclarationException(java.lang.String)
cons public ConstraintDeclarationException(java.lang.String,java.lang.Throwable)
cons public ConstraintDeclarationException(java.lang.Throwable)
supr javax.validation.ValidationException

CLSS public javax.validation.ConstraintDefinitionException
cons public ConstraintDefinitionException()
cons public ConstraintDefinitionException(java.lang.String)
cons public ConstraintDefinitionException(java.lang.String,java.lang.Throwable)
cons public ConstraintDefinitionException(java.lang.Throwable)
supr javax.validation.ValidationException

CLSS public abstract interface javax.validation.ConstraintValidator<%0 extends java.lang.annotation.Annotation, %1 extends java.lang.Object>
meth public abstract boolean isValid({javax.validation.ConstraintValidator%1},javax.validation.ConstraintValidatorContext)
meth public abstract void initialize({javax.validation.ConstraintValidator%0})

CLSS public abstract interface javax.validation.ConstraintValidatorContext
innr public abstract interface static ConstraintViolationBuilder
meth public abstract java.lang.String getDefaultConstraintMessageTemplate()
meth public abstract javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder buildConstraintViolationWithTemplate(java.lang.String)
meth public abstract void disableDefaultConstraintViolation()

CLSS public abstract interface static javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder
innr public abstract interface static NodeBuilderCustomizableContext
innr public abstract interface static NodeBuilderDefinedContext
innr public abstract interface static NodeContextBuilder
meth public abstract javax.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderDefinedContext addNode(java.lang.String)

CLSS public abstract interface static javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext
meth public abstract javax.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addNode(java.lang.String)
meth public abstract javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeContextBuilder inIterable()

CLSS public abstract interface static javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderDefinedContext
meth public abstract javax.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addNode(java.lang.String)

CLSS public abstract interface static javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeContextBuilder
meth public abstract javax.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addNode(java.lang.String)
meth public abstract javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderDefinedContext atIndex(java.lang.Integer)
meth public abstract javax.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderDefinedContext atKey(java.lang.Object)

CLSS public abstract interface javax.validation.ConstraintValidatorFactory
meth public abstract <%0 extends javax.validation.ConstraintValidator<?,?>> {%%0} getInstance(java.lang.Class<{%%0}>)
meth public abstract void releaseInstance(javax.validation.ConstraintValidator<?,?>)

CLSS public abstract interface javax.validation.ConstraintViolation<%0 extends java.lang.Object>
meth public abstract java.lang.Class<{javax.validation.ConstraintViolation%0}> getRootBeanClass()
meth public abstract java.lang.Object getInvalidValue()
meth public abstract java.lang.Object getLeafBean()
meth public abstract java.lang.String getMessage()
meth public abstract java.lang.String getMessageTemplate()
meth public abstract javax.validation.Path getPropertyPath()
meth public abstract javax.validation.metadata.ConstraintDescriptor<?> getConstraintDescriptor()
meth public abstract {javax.validation.ConstraintViolation%0} getRootBean()

CLSS public javax.validation.ConstraintViolationException
cons public ConstraintViolationException(java.lang.String,java.util.Set<javax.validation.ConstraintViolation<?>>)
cons public ConstraintViolationException(java.util.Set<javax.validation.ConstraintViolation<?>>)
meth public java.util.Set<javax.validation.ConstraintViolation<?>> getConstraintViolations()
supr javax.validation.ValidationException
hfds constraintViolations

CLSS public abstract interface !annotation javax.validation.ConvertGroup
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, CONSTRUCTOR, PARAMETER])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<?> from()
meth public abstract java.lang.Class<?> to()

CLSS public abstract interface static !annotation javax.validation.ConvertGroup$List
intf java.lang.annotation.Annotation
meth public abstract javax.validation.ConvertGroup[] value()

CLSS public abstract interface !annotation javax.validation.CrossParameterConstraint
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>> validatedBy()

CLSS public javax.validation.GroupDefinitionException
cons public GroupDefinitionException()
cons public GroupDefinitionException(java.lang.String)
cons public GroupDefinitionException(java.lang.String,java.lang.Throwable)
cons public GroupDefinitionException(java.lang.Throwable)
supr javax.validation.ValidationException

CLSS public abstract interface !annotation javax.validation.GroupSequence
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<?>[] value()

CLSS public abstract interface javax.validation.MessageInterpolator
innr public abstract interface static Context
meth public abstract java.lang.String interpolate(java.lang.String,javax.validation.MessageInterpolator$Context)
meth public abstract java.lang.String interpolate(java.lang.String,javax.validation.MessageInterpolator$Context,java.util.Locale)

CLSS public abstract interface static javax.validation.MessageInterpolator$Context
meth public abstract java.lang.Object getValidatedValue()
meth public abstract javax.validation.metadata.ConstraintDescriptor<?> getConstraintDescriptor()

CLSS public abstract interface javax.validation.MethodValidator
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<javax.validation.ConstraintViolation<{%%0}>> validateConstructorParameters(java.lang.reflect.Constructor<{%%0}>,java.lang.Object[],java.lang.Class<?>[])
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<javax.validation.ConstraintViolation<{%%0}>> validateConstructorReturnValue(java.lang.reflect.Constructor<{%%0}>,{%%0},java.lang.Class<?>[])
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<javax.validation.ConstraintViolation<{%%0}>> validateParameters({%%0},java.lang.reflect.Method,java.lang.Object[],java.lang.Class<?>[])
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<javax.validation.ConstraintViolation<{%%0}>> validateReturnValue({%%0},java.lang.reflect.Method,java.lang.Object,java.lang.Class<?>[])

CLSS public abstract interface !annotation javax.validation.OverridesAttribute
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault int constraintIndex()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> constraint()
meth public abstract java.lang.String name()

CLSS public abstract interface static !annotation javax.validation.OverridesAttribute$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.OverridesAttribute[] value()

CLSS public abstract interface javax.validation.ParameterNameProvider
meth public abstract java.lang.String[] getParameterNames(java.lang.reflect.Constructor<?>)
meth public abstract java.lang.String[] getParameterNames(java.lang.reflect.Method)

CLSS public abstract interface javax.validation.Path
innr public abstract interface static Node
intf java.lang.Iterable<javax.validation.Path$Node>

CLSS public abstract interface static javax.validation.Path$Node
meth public abstract boolean isInIterable()
meth public abstract java.lang.Integer getIndex()
meth public abstract java.lang.Object getKey()
meth public abstract java.lang.String getName()
meth public abstract javax.validation.metadata.ElementDescriptor getElementDescriptor()

CLSS public abstract interface javax.validation.Payload

CLSS public abstract interface !annotation javax.validation.ReportAsSingleViolation
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface javax.validation.TraversableResolver
meth public abstract boolean isCascadable(java.lang.Object,javax.validation.Path$Node,java.lang.Class<?>,javax.validation.Path,java.lang.annotation.ElementType)
meth public abstract boolean isReachable(java.lang.Object,javax.validation.Path$Node,java.lang.Class<?>,javax.validation.Path,java.lang.annotation.ElementType)

CLSS public javax.validation.UnexpectedTypeException
cons public UnexpectedTypeException()
cons public UnexpectedTypeException(java.lang.String)
cons public UnexpectedTypeException(java.lang.String,java.lang.Throwable)
cons public UnexpectedTypeException(java.lang.Throwable)
supr javax.validation.ConstraintDeclarationException

CLSS public abstract interface !annotation javax.validation.Valid
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation

CLSS public javax.validation.Validation
cons public Validation()
meth public static <%0 extends javax.validation.Configuration<{%%0}>, %1 extends javax.validation.spi.ValidationProvider<{%%0}>> javax.validation.bootstrap.ProviderSpecificBootstrap<{%%0}> byProvider(java.lang.Class<{%%1}>)
meth public static javax.validation.ValidatorFactory buildDefaultValidatorFactory()
meth public static javax.validation.bootstrap.GenericBootstrap byDefaultProvider()
supr java.lang.Object
hcls DefaultValidationProviderResolver,GenericBootstrapImpl,GetValidationProviderList,ProviderSpecificBootstrapImpl

CLSS public javax.validation.ValidationException
cons public ValidationException()
cons public ValidationException(java.lang.String)
cons public ValidationException(java.lang.String,java.lang.Throwable)
cons public ValidationException(java.lang.Throwable)
supr java.lang.RuntimeException

CLSS public abstract interface javax.validation.ValidationProviderResolver
meth public abstract java.util.List<javax.validation.spi.ValidationProvider<?>> getValidationProviders()

CLSS public abstract interface javax.validation.Validator
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<javax.validation.ConstraintViolation<{%%0}>> validate({%%0},java.lang.Class<?>[])
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<javax.validation.ConstraintViolation<{%%0}>> validateProperty({%%0},java.lang.String,java.lang.Class<?>[])
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<javax.validation.ConstraintViolation<{%%0}>> validateValue(java.lang.Class<{%%0}>,java.lang.String,java.lang.Object,java.lang.Class<?>[])
meth public abstract <%0 extends java.lang.Object> {%%0} unwrap(java.lang.Class<{%%0}>)
meth public abstract javax.validation.MethodValidator forMethods()
meth public abstract javax.validation.metadata.BeanDescriptor getConstraintsForClass(java.lang.Class<?>)

CLSS public abstract interface javax.validation.ValidatorContext
meth public abstract javax.validation.Validator getValidator()
meth public abstract javax.validation.ValidatorContext constraintValidatorFactory(javax.validation.ConstraintValidatorFactory)
meth public abstract javax.validation.ValidatorContext messageInterpolator(javax.validation.MessageInterpolator)
meth public abstract javax.validation.ValidatorContext parameterNameProvider(javax.validation.ParameterNameProvider)
meth public abstract javax.validation.ValidatorContext traversableResolver(javax.validation.TraversableResolver)

CLSS public abstract interface javax.validation.ValidatorFactory
meth public abstract <%0 extends java.lang.Object> {%%0} unwrap(java.lang.Class<{%%0}>)
meth public abstract javax.validation.ConstraintValidatorFactory getConstraintValidatorFactory()
meth public abstract javax.validation.MessageInterpolator getMessageInterpolator()
meth public abstract javax.validation.ParameterNameProvider getParameterNameProvider()
meth public abstract javax.validation.TraversableResolver getTraversableResolver()
meth public abstract javax.validation.Validator getValidator()
meth public abstract javax.validation.ValidatorContext usingContext()
meth public abstract void close()

CLSS public abstract interface javax.validation.bootstrap.GenericBootstrap
meth public abstract javax.validation.Configuration<?> configure()
meth public abstract javax.validation.bootstrap.GenericBootstrap providerResolver(javax.validation.ValidationProviderResolver)

CLSS public abstract interface javax.validation.bootstrap.ProviderSpecificBootstrap<%0 extends javax.validation.Configuration<{javax.validation.bootstrap.ProviderSpecificBootstrap%0}>>
meth public abstract javax.validation.bootstrap.ProviderSpecificBootstrap<{javax.validation.bootstrap.ProviderSpecificBootstrap%0}> providerResolver(javax.validation.ValidationProviderResolver)
meth public abstract {javax.validation.bootstrap.ProviderSpecificBootstrap%0} configure()

CLSS public abstract interface !annotation javax.validation.constraints.AssertFalse
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation javax.validation.constraints.AssertFalse$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.AssertFalse[] value()

CLSS public abstract interface !annotation javax.validation.constraints.AssertTrue
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation javax.validation.constraints.AssertTrue$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.AssertTrue[] value()

CLSS public abstract interface !annotation javax.validation.constraints.DecimalMax
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract java.lang.String value()

CLSS public abstract interface static !annotation javax.validation.constraints.DecimalMax$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.DecimalMax[] value()

CLSS public abstract interface !annotation javax.validation.constraints.DecimalMin
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract java.lang.String value()

CLSS public abstract interface static !annotation javax.validation.constraints.DecimalMin$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.DecimalMin[] value()

CLSS public abstract interface !annotation javax.validation.constraints.Digits
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract int fraction()
meth public abstract int integer()

CLSS public abstract interface static !annotation javax.validation.constraints.Digits$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.Digits[] value()

CLSS public abstract interface !annotation javax.validation.constraints.Future
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation javax.validation.constraints.Future$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.Future[] value()

CLSS public abstract interface !annotation javax.validation.constraints.Max
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract long value()

CLSS public abstract interface static !annotation javax.validation.constraints.Max$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.Max[] value()

CLSS public abstract interface !annotation javax.validation.constraints.Min
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract long value()

CLSS public abstract interface static !annotation javax.validation.constraints.Min$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.Min[] value()

CLSS public abstract interface !annotation javax.validation.constraints.NotNull
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation javax.validation.constraints.NotNull$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.NotNull[] value()

CLSS public abstract interface !annotation javax.validation.constraints.Null
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation javax.validation.constraints.Null$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.Null[] value()

CLSS public abstract interface !annotation javax.validation.constraints.Past
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation javax.validation.constraints.Past$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.Past[] value()

CLSS public abstract interface !annotation javax.validation.constraints.Pattern
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
innr public final static !enum Flag
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract !hasdefault javax.validation.constraints.Pattern$Flag[] flags()
meth public abstract java.lang.String regexp()

CLSS public final static !enum javax.validation.constraints.Pattern$Flag
fld public final static javax.validation.constraints.Pattern$Flag CANON_EQ
fld public final static javax.validation.constraints.Pattern$Flag CASE_INSENSITIVE
fld public final static javax.validation.constraints.Pattern$Flag COMMENTS
fld public final static javax.validation.constraints.Pattern$Flag DOTALL
fld public final static javax.validation.constraints.Pattern$Flag MULTILINE
fld public final static javax.validation.constraints.Pattern$Flag UNICODE_CASE
fld public final static javax.validation.constraints.Pattern$Flag UNIX_LINES
meth public int getValue()
meth public static javax.validation.constraints.Pattern$Flag valueOf(java.lang.String)
meth public static javax.validation.constraints.Pattern$Flag[] values()
supr java.lang.Enum<javax.validation.constraints.Pattern$Flag>
hfds value

CLSS public abstract interface static !annotation javax.validation.constraints.Pattern$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.Pattern[] value()

CLSS public abstract interface !annotation javax.validation.constraints.Size
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
 anno 0 javax.validation.Constraint(java.lang.Class<? extends javax.validation.ConstraintValidator<?,?>>[] validatedBy=[])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault int max()
meth public abstract !hasdefault int min()
meth public abstract !hasdefault java.lang.Class<? extends javax.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation javax.validation.constraints.Size$List
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract javax.validation.constraints.Size[] value()

CLSS public abstract interface javax.validation.groups.Default

CLSS public abstract interface javax.validation.metadata.BeanDescriptor
intf javax.validation.metadata.ElementDescriptor
meth public abstract !varargs javax.validation.metadata.ConstructorDescriptor getConstraintsForConstructor(java.lang.Class<?>[])
meth public abstract !varargs javax.validation.metadata.MethodDescriptor getConstraintsForMethod(java.lang.String,java.lang.Class<?>[])
meth public abstract boolean isBeanConstrained()
meth public abstract java.util.Set<javax.validation.metadata.ConstructorDescriptor> getConstrainedConstructors()
meth public abstract java.util.Set<javax.validation.metadata.MethodDescriptor> getConstrainedMethods()
meth public abstract java.util.Set<javax.validation.metadata.PropertyDescriptor> getConstrainedProperties()
meth public abstract javax.validation.metadata.PropertyDescriptor getConstraintsForProperty(java.lang.String)

CLSS public abstract interface javax.validation.metadata.ConstraintDescriptor<%0 extends java.lang.annotation.Annotation>
meth public abstract boolean isReportAsSingleViolation()
meth public abstract java.util.List<java.lang.Class<? extends javax.validation.ConstraintValidator<{javax.validation.metadata.ConstraintDescriptor%0},?>>> getConstraintValidatorClasses()
meth public abstract java.util.Map<java.lang.String,java.lang.Object> getAttributes()
meth public abstract java.util.Set<java.lang.Class<? extends javax.validation.Payload>> getPayload()
meth public abstract java.util.Set<java.lang.Class<?>> getGroups()
meth public abstract java.util.Set<javax.validation.metadata.ConstraintDescriptor<?>> getComposingConstraints()
meth public abstract {javax.validation.metadata.ConstraintDescriptor%0} getAnnotation()

CLSS public abstract interface javax.validation.metadata.ConstructorDescriptor
intf javax.validation.metadata.ElementDescriptor
meth public abstract boolean areParametersConstrained()
meth public abstract boolean isReturnValueConstrained()
meth public abstract java.util.List<javax.validation.metadata.ParameterDescriptor> getParameterDescriptors()
meth public abstract javax.validation.metadata.ReturnValueDescriptor getReturnValueDescriptor()

CLSS public abstract interface javax.validation.metadata.ElementDescriptor
innr public abstract interface static ConstraintFinder
innr public final static !enum Kind
meth public abstract <%0 extends javax.validation.metadata.ElementDescriptor> {%%0} as(java.lang.Class<{%%0}>)
meth public abstract boolean hasConstraints()
meth public abstract java.lang.Class<?> getElementClass()
meth public abstract java.util.Set<javax.validation.metadata.ConstraintDescriptor<?>> getConstraintDescriptors()
meth public abstract javax.validation.metadata.ElementDescriptor$ConstraintFinder findConstraints()
meth public abstract javax.validation.metadata.ElementDescriptor$Kind getKind()

CLSS public abstract interface static javax.validation.metadata.ElementDescriptor$ConstraintFinder
meth public abstract !varargs javax.validation.metadata.ElementDescriptor$ConstraintFinder declaredOn(java.lang.annotation.ElementType[])
meth public abstract !varargs javax.validation.metadata.ElementDescriptor$ConstraintFinder unorderedAndMatchingGroups(java.lang.Class<?>[])
meth public abstract boolean hasConstraints()
meth public abstract java.util.Set<javax.validation.metadata.ConstraintDescriptor<?>> getConstraintDescriptors()
meth public abstract javax.validation.metadata.ElementDescriptor$ConstraintFinder lookingAt(javax.validation.metadata.Scope)

CLSS public final static !enum javax.validation.metadata.ElementDescriptor$Kind
fld public final static javax.validation.metadata.ElementDescriptor$Kind BEAN
fld public final static javax.validation.metadata.ElementDescriptor$Kind CONSTRUCTOR
fld public final static javax.validation.metadata.ElementDescriptor$Kind METHOD
fld public final static javax.validation.metadata.ElementDescriptor$Kind PARAMETER
fld public final static javax.validation.metadata.ElementDescriptor$Kind PROPERTY
fld public final static javax.validation.metadata.ElementDescriptor$Kind RETURN_VALUE
meth public static javax.validation.metadata.ElementDescriptor$Kind valueOf(java.lang.String)
meth public static javax.validation.metadata.ElementDescriptor$Kind[] values()
supr java.lang.Enum<javax.validation.metadata.ElementDescriptor$Kind>

CLSS public abstract interface javax.validation.metadata.MethodDescriptor
intf javax.validation.metadata.ElementDescriptor
meth public abstract boolean areParametersConstrained()
meth public abstract boolean isReturnValueConstrained()
meth public abstract java.lang.String getName()
meth public abstract java.util.List<javax.validation.metadata.ParameterDescriptor> getParameterDescriptors()
meth public abstract javax.validation.metadata.ReturnValueDescriptor getReturnValueDescriptor()

CLSS public abstract interface javax.validation.metadata.ParameterDescriptor
intf javax.validation.metadata.ElementDescriptor
meth public abstract boolean isCascaded()
meth public abstract int getIndex()
meth public abstract java.lang.String getName()

CLSS public abstract interface javax.validation.metadata.PropertyDescriptor
intf javax.validation.metadata.ElementDescriptor
meth public abstract boolean isCascaded()
meth public abstract java.lang.String getPropertyName()

CLSS public abstract interface javax.validation.metadata.ReturnValueDescriptor
intf javax.validation.metadata.ElementDescriptor
meth public abstract boolean isCascaded()

CLSS public final !enum javax.validation.metadata.Scope
fld public final static javax.validation.metadata.Scope HIERARCHY
fld public final static javax.validation.metadata.Scope LOCAL_ELEMENT
meth public static javax.validation.metadata.Scope valueOf(java.lang.String)
meth public static javax.validation.metadata.Scope[] values()
supr java.lang.Enum<javax.validation.metadata.Scope>

CLSS public abstract interface javax.validation.spi.BootstrapState
meth public abstract javax.validation.ValidationProviderResolver getDefaultValidationProviderResolver()
meth public abstract javax.validation.ValidationProviderResolver getValidationProviderResolver()

CLSS public abstract interface javax.validation.spi.ConfigurationState
meth public abstract boolean isIgnoreXmlConfiguration()
meth public abstract java.util.Map<java.lang.String,java.lang.String> getProperties()
meth public abstract java.util.Set<java.io.InputStream> getMappingStreams()
meth public abstract javax.validation.ConstraintValidatorFactory getConstraintValidatorFactory()
meth public abstract javax.validation.MessageInterpolator getMessageInterpolator()
meth public abstract javax.validation.ParameterNameProvider getParameterNameProvider()
meth public abstract javax.validation.TraversableResolver getTraversableResolver()

CLSS public abstract interface !annotation javax.validation.spi.MethodValidated
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface javax.validation.spi.ValidationProvider<%0 extends javax.validation.Configuration<{javax.validation.spi.ValidationProvider%0}>>
meth public abstract javax.validation.Configuration<?> createGenericConfiguration(javax.validation.spi.BootstrapState)
meth public abstract javax.validation.ValidatorFactory buildValidatorFactory(javax.validation.spi.ConfigurationState)
meth public abstract {javax.validation.spi.ValidationProvider%0} createSpecializedConfiguration(javax.validation.spi.BootstrapState)

