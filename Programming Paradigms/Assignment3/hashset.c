#include "hashset.h"
#include <assert.h>
#include <stdlib.h>
#include <string.h>

void HashSetNew(hashset *h, int elemSize, int numBuckets,
		HashSetHashFunction hashfn, HashSetCompareFunction comparefn, HashSetFreeFunction freefn){
	assert(elemSize > 0);
	assert(numBuckets > 0);
	assert(hashfn != NULL);
	assert(comparefn != NULL);
	h->elemSize = elemSize;
	h->numElements = 0;
	h->numberOfBuckets = numBuckets;
	h->HashSetHashFunction = hashfn;
	h->HashSetCompareFunction = comparefn;
	h->buckets = malloc(numBuckets * sizeof(vector));
	for (int i = 0; i < numBuckets; ++i) {
		VectorNew(&h->buckets[i], elemSize, freefn, 8);
	}
}

void HashSetDispose(hashset *h) {
	for (int i = 0; i < h->numberOfBuckets; ++i) {
		VectorDispose(&h->buckets[i]);
	}
	free(h->buckets);
}

int HashSetCount(const hashset *h) {
	return h->numElements;	
}

void HashSetMap(hashset *h, HashSetMapFunction mapfn, void *auxData) {
	assert(mapfn != NULL);
	for (int i = 0; i < h->numberOfBuckets; ++i) {
		VectorMap(&h->buckets[i], mapfn, auxData);
	}
}

void HashSetEnter(hashset *h, const void *elemAddr) {
	assert(elemAddr != NULL);
	int hashCode = h->HashSetHashFunction(elemAddr, h->numberOfBuckets);
	assert(hashCode >= 0 && hashCode < h->numberOfBuckets);
	int ind = VectorSearch(&h->buckets[hashCode], elemAddr, h->HashSetCompareFunction, 0, false);
	if (ind != -1) {
		VectorReplace(&h->buckets[hashCode], elemAddr, ind);
	} else {
		VectorAppend(&h->buckets[hashCode], elemAddr);
		h->numElements++;
	}	
}

void *HashSetLookup(const hashset *h, const void *elemAddr) {
	assert(elemAddr != NULL);
	int hashCode = h->HashSetHashFunction(elemAddr, h->numberOfBuckets);
	assert(hashCode >= 0 && hashCode < h->numberOfBuckets);
	int	ind = VectorSearch(&h->buckets[hashCode], elemAddr, h->HashSetCompareFunction, 0, false);
	if (ind == -1) return NULL;
	return VectorNth(&h->buckets[hashCode], ind);
}
