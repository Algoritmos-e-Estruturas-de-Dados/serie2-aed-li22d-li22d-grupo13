package serie2.part3

class Node<T>(
    var value: T = Any() as T,
    var next: Node<T>? = null,
    var previous: Node<T>? = null
)

fun splitEvensAndOdds(list: Node<Int>) {
    val sentinel = list
    var current = sentinel.next

    // Continua até que o nó atual volte ao sentinela
    while (current !== sentinel) {
        val following = current!!.next

        // Se for par, move o nó para a frente da lista
        if (current.value % 2 == 0) {
            // Desvincula o nó da sua posição atual
            current.previous!!.next = current.next
            current.next!!.previous = current.previous

            // Coloca imediatamente após o sentinela
            current.previous = sentinel
            current.next = sentinel.next
            sentinel.next!!.previous = current
            sentinel.next = current
        }

        current = following
    }
}

fun <T> intersection(list1: Node<T>, list2: Node<T>, cmp: Comparator<T>): Node<T>? {
    var head: Node<T>? = null
    var tail: Node<T>? = null

    var ptr1 = list1.next
    var ptr2 = list2.next

    while (ptr1 != list1 && ptr2 != list2) {
        val result = cmp.compare(ptr1!!.value, ptr2!!.value)

        if (result == 0) {
            val valMatch = ptr1.value
            val next1 = ptr1.next
            val next2 = ptr2.next

            // Remove de ambas as listas
            ptr1.previous!!.next = ptr1.next
            ptr1.next!!.previous = ptr1.previous
            ptr2!!.previous!!.next = ptr2.next
            ptr2.next!!.previous = ptr2.previous

            ptr1.previous = tail
            ptr1.next = null

            if (head == null) {
                head = ptr1
                tail = ptr1
            } else {
                tail!!.next = ptr1
                tail = ptr1
            }

            var skip1 = next1
            while (skip1 != list1 && cmp.compare(skip1!!.value, valMatch) == 0) {
                skip1 = skip1.next
            }

            var skip2 = next2
            while (skip2 != list2 && cmp.compare(skip2!!.value, valMatch) == 0) {
                skip2 = skip2.next
            }

            ptr1 = skip1
            ptr2 = skip2
        } else if (result < 0) {
            ptr1 = ptr1.next
        } else {
            ptr2 = ptr2!!.next
        }
    }

    return head
}

// -------------------- Funçoes Auxiliares --------------------

fun <T> listaCircularComSentinela(values: List<T>): Node<T> {
    val sentinel = Node<T>()
    sentinel.next = sentinel
    sentinel.previous = sentinel

    values.forEach { valor ->
        val novo = Node(valor)
        novo.next = sentinel
        novo.previous = sentinel.previous
        sentinel.previous!!.next = novo
        sentinel.previous = novo
    }
    return sentinel
}

fun <T> printListaCircularComSentinela(sentinel: Node<T>) {
    var atual = sentinel.next
    while (atual !== sentinel) {
        print("${atual!!.value} ")
        atual = atual.next
    }
    println()
}

fun <T> printLinearList(list: Node<T>?) {
    var atual = list
    while (atual != null) {
        print("${atual.value} ")
        atual = atual.next
    }
    println()
}

fun randomIntList(size: Int, max: Int): List<Int> {
    return List(size) { (0..max).random() }
}

// -------------------- MAIN --------------------

fun main() {
    println("=== Teste: splitEvensAndOdds ===")
    val numerosAleatorios = randomIntList(10, 100)
    println("Original:")
    println(numerosAleatorios)

    val lista = listaCircularComSentinela(numerosAleatorios)
    splitEvensAndOdds(lista)

    println("Após reordenar pares no início:")
    printListaCircularComSentinela(lista)

    println("\n=== Teste: intersection ===")
    val l1 = listOf(7, 6, 16, 9, 2, 5, 14, 13, 3, 10).sorted()
    val l2 = listOf(8, 5, 6, 17, 12, 9, 14, 13, 18, 11).sorted()

    println("Lista 1: $l1")
    println("Lista 2: $l2")

    val c1 = listaCircularComSentinela(l1)
    val c2 = listaCircularComSentinela(l2)

    val resultado = intersection(c1, c2, Comparator { a, b -> a - b })
    println("Interseção:")
    printLinearList(resultado)
}
