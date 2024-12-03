<script setup>

import Cross from '../../assets/icons/Cross.vue'

const props = defineProps({
    icon : String
})

</script>

<template>
    <button class="bg-custom-red hover:bg-red-900 text-white font-CASlalomBold py-2 px-4 rounded-md duration-300">
        <div class="flex justify-center items-center gap-2">
            <span v-if="props.icon == 'disagree'"><Cross color="white" :width="18" /></span>
           <div class="leading-5">
                <slot />
           </div>
        </div>
    </button>
</template>