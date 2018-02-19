# Scala
__Nota__ puede que no sea la definición y haya errores al entenderlo


Aquí voy a ir haciendo un pequeño manualcillo de posibles casos y
soluciones

## Scrip
Podemos lanzar scala como scrip
```
scala nombre_fichero.scala
```

## Clases
Para definir una clase pondremos la palabra clave __class__ con ella
indicamos que es una clase nueva que podemos instanciar, si tiene
argunmentos para el constructor lo podemos indicar como si fuera un
metodo especificando el tipo de argumentos.
```scala
class Rational(n: Int, d: Int)
```
Si queremos tener constructores alternativos tenemos podemos crearlos
añadiendo __this__, el constructor primario es la principal entrada de
la clase, si queremos más constructores tendremos que llamar a este:
```scala
def this(n: Int) = this(n, 1)
```
Solamente el constructor primario puede invocal al de la superclase

Si queremos añadir métodos lo indicaremos como una función __def__ y si
por ejemplo queremos sobreescribir el método toString  
```scala
override def toString = n + "/" + d
```
Además puede que haya unos requisitos como en este caso que denominador
no puede ser 0, lo especificamos de la siguiente manera, tenemos dos
opciones que fallará en tiempo de ejecución:
```scala
// requermientos
require(d != 0) // lo evalua si es true sera correcto en caso contrario
// excepcion assert
assert(d != 0)
```
Si queremos tener acceso a los argumentos que hemos pasado, tenemos que
crear variables
```scala
val numer: Int = n
val denom: Int = d

def add(that: Rational): Rational =
  new Rational(
    numer * that.denom + that.numer * denom,
    denom * that.denom
  )
```
Las funciones o métodos por defectos son publicos
```scala
def lessThan(that: Rational): Boolean =
    this.numer * that.denom < that.numer * this.denom

def max(that: Rational): Rational =
    if (this.lessThan(that)) that else this
```
#### Campos y métodos privados
Si queremos no tener acceso a varios métodos o campos debemos poner
private, por ejemplo cuando creamos un objeto del que estamos viendo no
esta simplificado, por ello vamos a crear las modificaciones para que al
crearlo este simplificado:
```scala
private val g = gcd(n.abs, d.abs)
val numer = n / g
val denom = d / g

private def gcd(a: Int, b: Int): Int =
if (b == 0) a else gcd(b, a % b)
```

quedando la clase finalmente con lo que hemos visto:
```scala

```

### Case Class
http://www.alessandrolacava.com/blog/scala-case-classes-in-depth/

Las __case class__ son clases que por defecto tienen los getters,
setters (si definimos la varibale como __var__), equals, hascode y to
string de forma predeterminada
#### Multiples constructores
```scala
case class Num(a: Int, str: String)

object Num {
  def apply(str: String): Num = new Num(0, str)
  def apply(a: Int): Num = new Num(a, "default")
}
```

#### Curry en case class
Hemos creado un case clase algo particular ya que solamente tiene acceso
a text si quisieramos tener acceso a source tendriamos que añadir
val/var si queremos getters
```scala
case class Keyword(text: String)(source: String, foo: Int)
```
#### Otros métodos
Además tenemos otros métodos más cuando creamos una case class:
* __productArity: Int__ devuelve el tamaño de argumentos en la clase.
* __productElement(n: Int): Any__, devuelve el n-ésimo elemento de la
clase
* __productIterator: Iterator[Any]__ devuelve un iterador sobre todos
los elementos de este producto que, en el contexto de clase de caso, son
sus argumentos.
* __productPrefix: String__ devuelve una cadena utilizada en el
toStringmétodo de las clases derivadas. En este caso, es el nombre de la
clase.

## TDA
https://scalerablog.wordpress.com/2016/09/28/tipos-de-datos-algebraicos-en-scala/

Tipo de Datos Algebraico (TDA) nos referimos a sumas y productos de
tipo,la suma suele representar un OR y el producto un AND (como en el
álgebra de Boole).

La forma más sencilla de representar la __suma__ (también llamada
coproducto) de tipos, en un paradigma que soporte polimorfismo (en
general) y en Scala (en particular), no es sino la herencia .  
```
Animal = Cat + Dog
```
En Scala se traduciria a
```scala
sealed trait Animal
case object Cat extends Animal
case object Dog extends Animal
```

En cuanto al __producto__, podríamos definirlo como el conjunto de
atributos que componen una instancia de un cierto tipo.
```
Student = String * Int
```

```scala
case class Student(name: String, age: Int)
```
##### Ejemplo
http://www.alessandrolacava.com/blog/keep-your-code-clean-with-algebraic-data-types-adts/  
En este ejemplo vemos como crear usando TDA una implementación para no
tener Option   

```scala
sealed trait Ad

object Ad {
  final case class Standard(
      headline: String,
      description1: String,
      description2: String
  ) extends Ad

  final case class Expanded(
      headline: String,
      headline2: String,
      description1: String
  ) extends Ad
}

new  Ad.Standard("a","2","3")
Ad.Standard("a","2","3")
```

##### Ejemplo numeros pares
```scala
case class Even(value: Int) {
  require(value%2==0, "it's not even")
}
```
Esto da un error en tiempo de ejecución si pasamos un número impar

```scala
sealed abstract class Even(val value: Int)

case object Zero extends Even(0)

case class Next(previousEven: Even) extends Even(2 + previousEven.value)
```

## Enumerados
En este ejemplo vamos a ver como definir nuestros propios enumerados,
usando varias partes:
* __sealed__ para especificar que todo estará en el fichero
* __trait__ definir la interfaz como en Java
* __patter matching__
* __case object__ para heredar del trait  

Podemos ver que definimos el sealed trait con la variable, luego
definimos el objeto y escribimos la función apply que será la que se
ejecute por defecto y definimos los distintos casos heredando, en la
opción por defecto hemos puesto un nombre para poder mostrar el tipo en
la excepción.  
```scala
import Errores.Error

sealed trait Errores {
  val levelName: String
}

object Errores {
  def apply(name: String): Errores = {
    name.toLowerCase() match {
      case Error.levelName => Error
      case Warn.levelName => Warn
      case Info.levelName => Info
      case other => throw new IllegalArgumentException(s"Invalid error level name: $other")
    }
  }

  case object Error extends Errores {
    override val levelName = "error"
  }

  case object Warn extends Errores {
    override val levelName = "warn"
  }

  case object Info extends Errores {
    override val levelName = "info"
  }

}

Errores("INFO")
Errores("-INFO")
```

Otro ejemplo
```scala
sealed trait Weekday
case object Monday extends Weekday
case object Tuesday extends Weekday
case object Wednesday extends Weekday
case object Thursday extends Weekday
case object Friday extends Weekday
case object Saturday extends Weekday
case object Sunday extends Weekday

def comprobarDia(day: Weekday): Unit = {
  day match {
    case Monday => println("lunessss")
    .....
  }
}
```

## Patter Matching
La coincidencia de patrones es muy importante y tiene un gran poder:

### Argumentos
```scala
def parseArgument(arg: String) = arg match {
  case "-h" | "--help" => println("Llamar a help")
  case "-v" | "--version" => println("Llamar a version")
  case whatever => println(s"desconocido: $whatever")
}
```
### Tipo de clases
Como vemos a continuación la coincidencia la podemos aplicar para
distinguir clases:
```scala
abstract class Device
case class Phone(model: String) extends Device{
  def screenOff = "Turning screen off"
}
case class Computer(model: String) extends Device {
  def screenSaverOn = "Turning screen saver on..."
}

def goIdle(device: Device) = device match {
  case p: Phone => p.screenOff
  case c: Computer => c.screenSaverOn
}
```

```scala
def f(x: Any): String = x match {
  case i: Int => "integer: " + i
  case _: Double => "a double"
  case s: String => "I want to say " + s
}
```
### Guardas


### Anotación @switch
Con esta anotación estamos diciendo que se usará como en java, pero
debemos tener unas consideraciones:
1. El valor coincidente debe ser un número entero conocido.
2. La expresión coincidente debe ser "simple". No puede contener ningún
tipo de verificaciones, declaraciones ni extractores.
3. La expresión también debe tener su valor disponible en tiempo de
compilación.
4. Debe haber más de dos declaraciones de casos.

Veamos un ejemplo
```scala
i match {
  case 1  => println("January")
  case 2  => println("February")
  case 3  => println("March")
  case 4  => println("April")
  case 5  => println("May")
  case 6  => println("June")
  case 7  => println("July")
  case 8  => println("August")
  case 9  => println("September")
  case 10 => println("October")
  case 11 => println("November")
  case 12 => println("December")
  // defecto
  case whoa  => println("Unexpected case: " + whoa.toString)
}
```


## Patrones de diseño
http://codecriticon.com/introduccion-patrones-diseno/

### Singleton
Asegura que una determinada clase sea instanciada una y sólo una vez,
proporcionando un único punto de acceso global a ella.

Métodos y valores que no están asociados con instancias individuales de
una clase se denominan objetos singleton y se denotan con la palabra
reservada object en vez de class.
```scala
object CashRegister {
    def open { println("opened") }
    def close { println("closed") }
}
```

También podemos tener unas funciones que hagan llamada a otra privada
para poder tener diferentes implementaciones
```scala
import java.util.Calendar
import java.text.SimpleDateFormat
object DateUtils {

    // as "Thursday, November 29"
    def getCurrentDate: String = getCurrentDateTime("EEEE, MMMM d")

    // as "6:20 p.m."
    def getCurrentTime: String = getCurrentDateTime("K:m aa")

    // a common function used by other date/time functions
    private def getCurrentDateTime(dateTimeFormat: String): String = {
        val dateFormat = new SimpleDateFormat(dateTimeFormat)
        val cal = Calendar.getInstance()
        dateFormat.format(cal.getTime())
    }

}
```
### Builder
Separa la construcción de un objeto complejo de su representación, de
forma que el mismo proceso de construcción pueda crear diferentes
representaciones. Simplifica la construcción de objetos con estructura
interna compleja y permite la construcción de objetos paso a paso.
```scala
case class Pizza(ingredients: Traversable[String], base: String = "Normal", topping: String = "Mozzarella")

val p1 = Pizza(Seq("Ham", "Mushroom"))

val p2 = Pizza(Seq("Mushroom"), topping = "Edam")

val p3 = Pizza(Seq("Ham", "Pineapple"), topping = "Edam", base = "Small")   

// Podemos copiar un obj cambiando el atributo
val lp2 = p3.copy(base = "Large")
```
#### Builder Pattern in Scala with Phantom Types
La razón por la que se llaman fantasmas es que se usan como
restricciones de tipo pero nunca se crean instancias  
https://medium.com/@maximilianofelice/builder-pattern-in-scala-with-phantom-types-3e29a167e863  
http://blog.rafaelferreira.net/2008/07/type-safe-builder-pattern-in-scala.html  
http://danielwestheide.com/blog/2015/06/28/put-your-writes-where-your-master-is-compile-time-restriction-of-slick-effect-types.html

#### Ejemplo Whisky

#### Ejemplo Pizza
```scala
object Chef {

  sealed trait Pizza
  object Pizza {
    sealed trait EmptyPizza extends Pizza
    sealed trait Cheese extends Pizza
    sealed trait Topping extends Pizza
    sealed trait Dough extends Pizza

    type FullPizza = EmptyPizza with Cheese with Topping with Dough
  }
}

case class Food(ingredients: Seq[String])

class Chef[Pizza <: Chef.Pizza](ingredients: Seq[String] = Seq()) {
  import Chef.Pizza._

  def addCheese(cheeseType: String): Chef[Pizza with Cheese]
    = new Chef(ingredients :+ cheeseType)

  def addTopping(toppingType: String): Chef[Pizza with Topping]
    = new Chef(ingredients :+ toppingType)

  def addDough: Chef[Pizza with Dough] = new Chef(ingredients :+ "dough")

  def build(implicit ev: Pizza =:= FullPizza): Food = Food(ingredients)
}

new Chef()
  .addCheese("mozzarella")
  .addDough
  .addTopping("olives")
  .build
```

#### Ejemplo puertas cerradas/abiertas
En este ejemplo implementamos un modelo muy simple, una puerta que puede
abrirse o cerrarse, y tratamos de usar los fantasmas para garantizar, en
tiempo de compilación, que solo podemos abrir una puerta cerrada y
cerrar una puerta abierta.

Comenzamos definiendo los estados Openy las Closeddos subclases de
Status, después de que definimos el Doorrasgo como, la parte interesante
es que agregamos al rasgo un parámetro de tipo que representa el estado
de la puerta.

Ponemos entonces el comportamiento en el objeto complementario, ahora
para cada acción agregamos una restricción de tipo, el método opentiene
la restricción Closedy closetiene Open.

Eso es más o menos, podemos ver en el ejemplo que las primeras 3
operaciones funcionan como se espera y luego, cuando tratamos de abrir
una Openpuerta o cerrarla Closed, no compila, que es lo que queríamos.
Es importante notar que los rasgos Openy Closednunca se crean
instancias, los usamos solo como restricciones.
```scala
trait Status
trait Open extends Status
trait Closed extends Status

trait Door[S <: Status]
object Door {
  def apply[S <: Status] = new Door[S] {}

  def open[S <: Closed](d: Door[S]) = Door[Open]
  def close[S <: Open](d: Door[S]) = Door[Closed]
}

val closedDoor = Door[Closed]
val openDoor = Door.open(closedDoor)
val closedAgainDoor = Door.close(openDoor)
```


### Indetificador  Notación:  
```scala
case class Foo[A](a:A) { // 'A' can be substituted with any type
    // getStringLength can only be used if this is a Foo[String]
    def getStringLength(implicit evidence: A =:= String) = a.length
}
```

```scala
class <%<[-From, +To] extends (From) ⇒ To
class <:<[-From, +To] extends (From) ⇒ To
class =:=[From, To] extends (From) ⇒ To
```
  * __A =:= B__ significa que A debe ser exactamente B
  * __A <:< B__ significa que A debe ser un subtipo de B (análogo a la
restricción de tipo simple<: ). Típicamente se usa como un parámetro de
evidencia
  * __A <%< B__ significa que A debe poder visualizarse como B,
posiblemente a través de una conversión implícita (análoga a la
restricción de tipo simple <%)

### Factory Method
Define una interfaz para crear un objeto, pero deja que sean las
subclases quienes decidan qué clase instanciar. Permite que una clase
delegue en sus subclases la creación de objetos.
### Object Pool
Administra la reutilización de objetos cuando un tipo de objetos es caro
de crear o solamente un número limitado de objetos puede ser creado.
### Lazy initialization
Proporciona un camino para retrasar la creación de un objeto o cualquier
otro proceso que sea costoso hasta que se necesita por primera vez.
### Multiton
Expande en el concepto Singleton para gestionar un mapa de instancias de
clases nombradas como pares clave-valor, y proporciona un punto de
acceso a ellos.
