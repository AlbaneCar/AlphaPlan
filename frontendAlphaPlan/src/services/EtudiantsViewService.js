import { customFetch } from '../utils/FetchUtil';

export async function fetchData(nobodyRef, loadingRef, loadedRef, dataRef, pageNumber = 0, pageSize = 14) {    
    loadedRef.value = !loadingRef.value && !loadedRef.value;
    pageNumber = pageNumber < 0 ? 0 : pageNumber;
    pageNumber = dataRef.value.totalPages && pageNumber > dataRef.value.totalPages - 1 ? dataRef.value.totalPages - 1 : pageNumber;

    try {
        const response = await customFetch(`users/students?size=${pageSize}&page=${pageNumber}`);
        if (!response.ok) {
            nobodyRef.value = true;
            throw new Error('Network response was not ok');
        }
        dataRef.value = await response.json();
        if (dataRef.value.totalElements === 0) {
            nobodyRef.value = true;
        } else {
            nobodyRef.value = false;
        }
    } catch (err) {
        nobodyRef.value = true;

    } finally {
        loadingRef.value = false;
        loadedRef.value = true;
    }
}
