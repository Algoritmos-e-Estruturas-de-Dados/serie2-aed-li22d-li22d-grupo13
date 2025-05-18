package serie2.part1_2
import kotlin.random.Random

/*
Determina o menor valor presente nas folhas de um max heap.
Num max heap, os menores valores encontram-se entre as folhas,
que estão localizadas a partir do índice heapSize / 2 até ao fim do array.
Esta função faz uma varredura por esses elementos para identificar o mínimo.
*/

fun minimum(maxHeap: Array<Int>, heapSize: Int): Int {

    // Inicialização do valor mínimo com a primeira folha
    var min = maxHeap[heapSize shr 1]

    var i = heapSize shr 1
    while (i < heapSize) {
        val current = maxHeap[i]
        if (current < min) min = current
        i++
    }

    // Devolve o menor valor encontrado entre as folhas
    return min
}
