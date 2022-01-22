import org.junit.Test
import top.hoshino9.controlflow.*
import top.hoshino9.controlflow.FlowResult.*
import kotlin.test.assertEquals

class SimpleTests {
    @Test
    fun ifTest0() {
        val result = moshi(true) {
            yield(1)
        }

        assertEquals(Success(1), result)
    }

    @Test
    fun ifTest1() {
        val result = moshi(false) {
            yield(1)
        }

        assertEquals(Cancelled, result)
    }

    @Test
    fun elseTest0() {
        val result = moshi(true) {
            yield(1)
        } hoka {
            yield(2)
        }

        assertEquals(Success(1), result)
    }

    @Test
    fun elseTest1() {
        val result = moshi(false) {
            yield(1)
        } hoka {
            yield(2)
        }

        assertEquals(Success(2), result)
    }

    @Test
    fun elseIfTest0() {
        val result = moshi(true) {
            yield(1)
        }.hokaMoshi(true) {
            yield(2)
        }

        assertEquals(Success(1), result)
    }

    @Test
    fun elseIfTest1() {
        val result = moshi(false) {
            yield(1)
        }.hokaMoshi(true) {
            yield(2)
        }

        assertEquals(Success(2), result)
    }

    @Test
    fun elseIfTest2() {
        val result = moshi(false) {
            yield(1)
        }.hokaMoshi(false) {
            yield(2)
        }

        assertEquals(Cancelled, result)
    }

    @Test
    fun loopTest0() {
        var i = 0

        val result = loop({ i < 5 }) {
            i += 1

            susumu()
        }

        assertEquals(Cancelled, result)
        assertEquals(5, i)
    }
}