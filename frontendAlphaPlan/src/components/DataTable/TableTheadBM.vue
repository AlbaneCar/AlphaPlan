<script setup>

defineProps({
    colonnes: Array
})

</script>

<template>
    <thead class="text-sm text-gray-700 bg-gray-100 font-CASlalomBold">
        <tr>
            <th class="px-6 py-3">{{ colonnes[0] }}</th>
            <th class="px-12 py-3">{{ colonnes[1] }}</th>
            <th class="px-6 py-3 text-end">{{ colonnes[2] }}</th>
        </tr>
    </thead>
</template>