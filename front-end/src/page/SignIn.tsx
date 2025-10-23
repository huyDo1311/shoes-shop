import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Link, Navigate, useLocation, useNavigate } from "react-router-dom";
import { Form } from "@/components/ui/form";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import RenderFormField from "@/components/shared/RenderFormField";
import { toast } from "sonner";
import { useMutation } from "@tanstack/react-query";
import authApi from "@/apis/auth.api";
import { useAuth } from "@/hooks/useAuth";



const SignIn = () => {
  const SignInSchema = z.object({
    email: z.string().refine((val) => /\S+@\S+\.\S+/.test(val), "Email không hợp lệ"),
    password: z.string().min(5, "Mật khẩu phải có ít nhất 5 ký tự"),
  });

  type SignInType = z.infer<typeof SignInSchema>;

  const form = useForm<SignInType>({
    resolver: zodResolver(SignInSchema),
  });

  // const signInAccountMutation = useMutation({
  //   mutationFn: (body: SignInType) => authApi.signin(body)
  // })

  const navigate = useNavigate();
  const { updateCurrentUser, setAuth, currentUser } = useAuth();

  const signInAccountMutation = useMutation({
  mutationFn: (body: SignInType) => authApi.signin(body),

  onSuccess: (result) => {
    const userProfile = result.data.data.user;

    updateCurrentUser({
      photoUrl: userProfile.avatar ?? "",
      name: userProfile.name ?? "Unknown",
      email: userProfile.email,
      id: userProfile._id,
      role: userProfile.roles[0] ?? "USER",
    });

    setAuth({
      accessToken: result.data.data.access_token,
    });

    toast.success(result.data.message);

  
    navigate("/");
  },

  onError: (error: any) => {
    toast.error(error.response?.data?.message || "Đăng nhập thất bại");
  },
});

const submit = async (data: SignInType) => {
  await signInAccountMutation.mutateAsync(data);
};


  return (
    <section className="py-8 ">
      <div className="max-w-lg w-full mx-auto ">
        <Card className="w-full ">
          <CardHeader>
            <CardTitle className="text-2xl">Login</CardTitle>
            <CardDescription>
              Enter your email below to login to your account
            </CardDescription>
          </CardHeader>
          <CardContent>
            {/* Form */}

            <Form {...form}>
              <form
                onSubmit={form.handleSubmit(submit)}
                className="grid gap-4 text-left"
                noValidate
              >
                <div className="grid gap-2 ">
                  <RenderFormField
                    name="email"
                    title="Email"
                    type="input"
                    inputType="email"
                    placeholder="abc@gmail.com"
                    control={form.control}
                  />
                </div>
                <div className="grid gap-2">
                  <RenderFormField
                    name="password"
                    title="Password"
                    type="input"
                    inputType="password"
                    control={form.control}
                  />
                </div>
                <div className="flex items-center">
                  <Link
                    to="/forgot"
                    className="ml-auto hover:text-sky-400 inline-block text-sm underline"
                  >
                    Forgot your password?
                  </Link>
                </div>
                <Button type="submit" className="w-full">
                  Login
                </Button>
              </form>
            </Form>
            {/* End form */}
            <div className="mt-4 text-center text-sm">
              Don&apos;t have an account?{" "}
              <Link to="/sign-up" className="underline hover:text-sky-400">
                Sign up
              </Link>
            </div>
          </CardContent>
        </Card>
      </div>
    </section>
  );
};

export default SignIn;
