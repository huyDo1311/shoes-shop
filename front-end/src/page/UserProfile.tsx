// import UserForm from "@/components/shared/UserForm";
// import { Button } from "@/components/ui/button";
// import {
//   Card,
//   CardContent,
//   CardDescription,
//   CardHeader,
//   CardTitle,
// } from "@/components/ui/card";

// import { Link } from "react-router-dom";
// import { toast } from "sonner";

// const UserProfile = () => {

//   const onSuccess = () => {
//     toast.success("Updated successfully");
//   };


//   // if (isLoading) {
//   //   return <div>Loading...</div>;
//   // }
//   // if (isError) {
//   //   return <div>Error...</div>;
//   // }
//   // const handleSubmit = (formValue: UserFormType) => {
//   //   return mutate({ data: formValue, type: "put" });
//   // };

//   return (
//     isSuccess && (
//       <section className="py-8">
//         <Card className="max-w-xl w-full mx-auto">
//           <CardHeader>
//             <CardTitle>Your Information</CardTitle>
//             <CardDescription>
//               Review and update your information
//             </CardDescription>
//             <CardContent>
//               <div className="p-4 flex items-center gap-3">
//                 <img
//                   src={data?.photoURL || "/user.png"}
//                   alt="user"
//                   className="object-cover size-20 rounded-sm"
//                 />
//                 <Link
//                   to={"/user/update-avatar"}
//                   className="text-sm text-sky-500"
//                 >
//                   Change your profile picture
//                 </Link>
//               </div>
//               <UserForm
//                 onSubmit={handleSubmit}
//                 isPending={isPending}
//                 defaultValues={data}
//                 isRegister={false}
//               />
//             </CardContent>
//           </CardHeader>
//         </Card>
//       </section>
//     )
//   );
// };

// export default UserProfile;
