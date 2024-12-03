<script setup>
  import { Users } from 'lucide-vue-next';
  import { MoveRight } from 'lucide-vue-next';
  import { BookA } from 'lucide-vue-next';
  import { BookText } from 'lucide-vue-next';

const props=defineProps({
    icon : String,
    disabled : Boolean
})
</script>

<template>
    <div v-if="!disabled" class="flex w-full h-full justify-start items-center rounded-md shadow-md bg-custom-lightblueAP p-2 hover:bg-custom-darkblueAP cursor-pointer duration-300">
        <div class="flex-1 flex justify-center items-center text-xl text-white font-CASlalomBold gap-2">
            <Users v-if="props.icon == 'equipe'"/>
            <BookA v-if="props.icon == 'evaluer'"/>
            <BookText v-if="props.icon == 'notes'"/>
            <slot />
        </div>
    </div>
    <div v-else class="flex w-full h-full justify-start items-center rounded-md shadow-md bg-custom-sideBarBG p-2 cursor-not-allowed">
        <div class="flex-1 flex justify-center items-center text-xl text-gray-400 font-CASlalomBold gap-2">
            <Users v-if="props.icon == 'equipe'"/>
            <BookA v-if="props.icon == 'evaluer'"/>
            <BookText v-if="props.icon == 'notes'"/>
            <slot />
        </div>
    </div>
</template>
