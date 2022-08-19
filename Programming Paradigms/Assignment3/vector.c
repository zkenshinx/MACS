#include "vector.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <search.h>
#include <assert.h>


static void grow(vector *v) {
    v->allocatedLen = v->allocatedLen + v->initialAllocation;
    v->elems = realloc(v->elems, v->allocatedLen * v->elemSize);
    assert(v->elems != NULL);
}

static void* getNthPosition(const vector *v, int position) {
    return (char*) v->elems + position * v->elemSize;
}

void VectorNew(vector *v, int elemSize, VectorFreeFunction freeFn, int initialAllocation){
    assert(elemSize > 0);
    assert(initialAllocation >= 0);
    if (initialAllocation == 0) initialAllocation = 4;
    v->logicalLen = 0;
    v->allocatedLen = initialAllocation;
    v->initialAllocation = initialAllocation;
    v->VectorFreeFunction = freeFn;    
    v->elemSize = elemSize;
    v->elems = malloc(elemSize * initialAllocation);
    assert(v->elems != NULL); 
}

void VectorDispose(vector *v) {
    if (v->VectorFreeFunction != NULL) {
        for (int i = 0; i < v->logicalLen; ++i) {
            void *elemAddr = getNthPosition(v, i);
            v->VectorFreeFunction(elemAddr);
        }
    }
    free(v->elems);
}

int VectorLength(const vector *v) {
    return v->logicalLen;
}

void *VectorNth(const vector *v, int position) {
    assert(position >= 0 && position < v->logicalLen);
    return getNthPosition(v, position);
}

void VectorReplace(vector *v, const void *elemAddr, int position) {
    assert(position >= 0 && position < v->logicalLen);
    void *dest = getNthPosition(v, position);
    if (v->VectorFreeFunction != NULL) v->VectorFreeFunction(dest);
    memcpy(dest, elemAddr, v->elemSize);
}

void VectorInsert(vector *v, const void *elemAddr, int position) {
    assert(position >= 0 && position <= v->logicalLen);
    if (v->logicalLen == v->allocatedLen) {
        grow(v);
    }
    void *insertDest = getNthPosition(v, position);
    void *shiftDest = (char*)insertDest + v->elemSize;
    size_t num = (v->logicalLen - position) * v->elemSize;
    memmove(shiftDest, insertDest, num);
    memcpy(insertDest, elemAddr, v->elemSize);
    v->logicalLen++;
}

void VectorAppend(vector *v, const void *elemAddr) {
    if (v->logicalLen == v->allocatedLen) {
        grow(v);
    }
    void *dest = getNthPosition(v, v->logicalLen);
    memcpy(dest, elemAddr, v->elemSize);
    v->logicalLen++;
}

void VectorDelete(vector *v, int position) {
    assert(position >= 0 && position < v->logicalLen);
    void *dest = getNthPosition(v, position);
    if (v->VectorFreeFunction != NULL) v->VectorFreeFunction(dest);
    void *source = (char*) dest + v->elemSize;
    size_t num = (v->logicalLen - position - 1) * v->elemSize;
    memmove(dest, source, num);
    v->logicalLen--;
}

void VectorSort(vector *v, VectorCompareFunction compare) {
    assert(compare != NULL);
    qsort(v->elems, v->logicalLen, v->elemSize, compare);
}

void VectorMap(vector *v, VectorMapFunction mapFn, void *auxData) {
    assert(mapFn != NULL);
    for (int i = 0; i < v->logicalLen; ++i) {
        mapFn(getNthPosition(v, i), auxData);
    }
}

static const int kNotFound = -1;
int VectorSearch(const vector *v, const void *key, VectorCompareFunction searchFn, int startIndex, bool isSorted) { 
    assert(startIndex >= 0 && startIndex <= v->logicalLen);
    assert(searchFn != NULL);
    assert(key != NULL);
    void* elemAddr;
    void* base = getNthPosition(v, startIndex);
    size_t nmemb = v->logicalLen - startIndex;
    if (isSorted) {
        elemAddr = bsearch(key, base, nmemb, v->elemSize, searchFn);
    } else {
        elemAddr = lfind(key, base, &nmemb, v->elemSize, searchFn);
    }
    if (elemAddr == NULL) return kNotFound;
    return ((char*)elemAddr - (char*)v->elems) / v->elemSize;
} 
