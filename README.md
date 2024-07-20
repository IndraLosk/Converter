Converter
_____

Мобильное приложение для конвертации валют с использованием реактивного подхода. Для работы необходимо ввести в поле ввода сумму для конвертации, из выпадающего списка выбрать валюту и нажать на кнопку "Рассчитать". 
Можно вводить как целые числа, так и числа с плавающей точкой. На втором экране можно увидеть сконвертированную сумму в рублях.
<div>
  <img src="https://github.com/IndraLosk/Converter/blob/main/img_for_readme/spinner.jpg" width="180" height="400" alt="Spinner">
  <img src="https://github.com/IndraLosk/Converter/blob/main/img_for_readme/app_work_1.jpg" width="180" height="400" alt="Image first screen">
  <img src="https://github.com/IndraLosk/Converter/blob/main/img_for_readme/app_work_2.jpg" width="180" height="400" alt="Image second screen">
</div>

<h2>Получение данных</h2>

Для получения данных было подключено несколько библиотек, среди которых: Retrofit - библиотека для работы с сетевыми запросами в Android-приложениях, converter-gson для преобразования JSON-данных в Kotlin-объекты и обратно, okhttp3:logging-interceptor - библиотека для логирования сетевых запросов и ответов и несколько библиотек для работы с корутинами в Kotlin. 

```Kotlin
implementation ("com.squareup.retrofit2:retrofit:2.9.0")
implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
implementation ("com.squareup.okhttp3:logging-interceptor:4.5.0")
implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")
implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
```

Для получения данных о валюте использовался JSON-файл по ссылке https://www.cbr-xml-daily.ru/daily_json.js, содержащий информацию о курсах валют, предоставляемую Центральным банком Российской Федерации.

<h2>Обработка исключений</h2>

В случае отсутствия интернета всплывет информационное сообщение, сообщающее о невозможности выполнения операции подсчета. Также не исключена возможность ошибки при вводе суммы для конвертации. Несмотря на то что изначально разрешенно вводить только численные символы, пользователь может не ввести ничего, получив сообщение об ошибке.
<div>
  <img src="https://github.com/IndraLosk/Converter/blob/main/img_for_readme/catch_mistake_internet.jpg" width="180" height="400" alt="catch_mistake_internet">
  <img src="https://github.com/IndraLosk/Converter/blob/main/img_for_readme/catch_mistake_input.jpg" width="180" height="400" alt="catch_mistake_input">
</div>

<h2>Красивое дополнение</h2>

Для красоты представления была добавлена анимация переходов между экранами, а также приложение расчитано для ввода очень длинных чисел в поле ввода, в этом случае текст на экране вывода уменьшится, не занимая и не выходя за границы экрана.

```xml
<TextView
            android:id="@+id/textNumCod"
            android:text="TextView"
            android:gravity="center"
            android:layout_width="match_parent"
            android:layout_height="60dp"
            app:autoSizeTextType="uniform"
            app:autoSizeMinTextSize="12sp"
            app:autoSizeMaxTextSize="100sp"
            app:autoSizeStepGranularity="2sp"
            android:textColor="@color/black" />
```

<h2>Код</h2>

Основная функция всего приложения, вызывая метод getCourses, получает курс валют, и в случае успешности выполнения операции проверяет, какая валюта была запрошена, присваивая значение переменной value. Если и на данном этапе не произошло проблем, то текущий курс умножается на запрашиваеммую сумму и возвращает результат, а также саму сумму и валюту для того чтобы передать на следующий экран и вывести там ответ. 

```Kotlin
fun getCourseValue(courseValue:String): Array<String> {
        var res = 0.0
        var value: Double?
        runBlocking {
            val repository = Repository()
            val response = repository.getCourses()
            if (response.isSuccessful) {
                val courses = response.body()
                value = when (courseValue) {
                    "USD" -> courses?.Valute?.USD?.Value
                    "EUR" -> courses?.Valute?.EUR?.Value
                    "GBP" -> courses?.Valute?.GBP?.Value
                    else -> null
                }
                if(value!=null)
                    res = value.toString().toDouble() * editText.text.toString().toDouble()
            }
        }
        return arrayOf(String.format("%.2f", res), editText.text.toString(), courseValue)
    }
```

Передача осуществляется с помощью встроенного класса Intent и его методами putExtra и getStringExtra.
