<script setup>

defineProps({
    colonnes: Array
})

</script>

<template>
    <thead class="text-xs text-gray-700 uppercase bg-gray-100 border-b  font-CASlalomBold">
        <tr>
            <th v-for="colonne in colonnes" :key="colonne" class="px-6 py-3" v-bind:class="{ 'text-end': colonne === 'Notes' || colonne === 'Votre B/M' }">
                {{ colonne === 'more' ? '' : colonne }}
            </th>
        </tr>
    </thead>
</template>